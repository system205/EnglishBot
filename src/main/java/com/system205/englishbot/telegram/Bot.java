package com.system205.englishbot.telegram;

import com.system205.englishbot.dto.Notification;
import com.system205.englishbot.entity.Word;
import com.system205.englishbot.services.UserService;
import com.system205.englishbot.telegram.command.BotCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
@EnableScheduling
public class Bot extends TelegramLongPollingBot implements TelegramBot{
    private final Long ownerId;
    private final UserService userService;

    private final Map<String, BotCommand> commands;

    public Bot(@Value("${bot.token}") String botToken, @Value("${bot.owner-id}") Long ownerId, UserService userService, Map<String, BotCommand> commands) {
        super(botToken);
        this.ownerId = ownerId;
        this.userService = userService;
        this.commands = commands;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            final String text = update.getMessage().getText();
            final User user = update.getMessage().getFrom();

            switch (text){
                case "/start" -> {
                    log.info("User#{} sent /start", user.getId());
                    sendMessage(ownerId, "%s#%s sent /start".formatted(user.getUserName() , user.getId()), false);
                }
                case "/add_words" -> sendMessage(user.getId(),
                    """
                        Reply with the lines of words:
                        milk - молоко
                        apple - яблоко
                        """, true);
                case "/show_words" -> {
                    final Set<Word> userWords = this.userService.getWordsByUserId(user.getId());
                    sendMessage(user.getId(), userWords.toString(), false);
                }
                default -> {
                    commands.getOrDefault(text, BotCommand.DoNothingCommand).execute(update);
                    // Handle saving words on reply
                    if (update.getMessage().isReply()) {
                        this.userService.addWordsToUser(user.getId(),
                            Arrays.stream(text.split("\n"))
                            .map(s -> s.split(" - "))
                            .map(s -> new Word(s[0], s[1]))
                            .toList()
                        );
                    }
                }
            }
        }
    }

    @Scheduled(fixedRateString = "${bot.scheduling.rate}")
    private void notifyAllUsersIfNeeded() {
        List<Notification> notifications = this.userService.getScheduledNotifications();
        log.info("Will notify {} users", notifications.size());
        for (Notification notification : notifications) {
            sendMessage(notification.getUser().getId(), notification.getText(), false);
        }
    }


    private void sendMessage(Long chatId, String text, boolean forceReply) {
        final SendMessage message = new SendMessage(String.valueOf(chatId), text);
        if (forceReply) message.setReplyMarkup(new ForceReplyKeyboard());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error when sending message", e);
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "EnglishBot";
    }

    @Override
    public void onRegister() {
        super.onRegister();
        sendMessage(ownerId, "Bot was successfully registered.");
    }

    @Override
    public void sendMessage(Long chatId, String text) {
        sendMessage(chatId, text, false);
    }
}
