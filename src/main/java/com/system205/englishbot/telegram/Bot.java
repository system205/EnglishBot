package com.system205.englishbot.telegram;

import com.system205.englishbot.entity.EnglishUser;
import com.system205.englishbot.entity.Word;
import com.system205.englishbot.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {
    private final Long ownerId;
    private final UserService userService;

    public Bot(@Value("${bot.token}") String botToken, @Value("${bot.owner-id}") Long ownerId, UserService userService) {
        super(botToken);
        this.ownerId = ownerId;
        this.userService = userService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            final String text = update.getMessage().getText();
            final User user = update.getMessage().getFrom();

            switch (text){
                case "/start" -> log.info("User#{} sent /start", user.getId());
                case "/add_words" -> sendMessage(user.getId(),
                    """
                        Reply with the lines of words:
                        milk - молоко
                        apple - яблоко
                        """, true);
                case "/show_words" -> {
                    EnglishUser englishUser = this.userService.getUser(user.getId());
                    sendMessage(user.getId(), englishUser.getWords().toString(), false);

                }
                default -> {
                    // Handle saving words on reply
                    if (update.getMessage().isReply()) {
                        EnglishUser englishUser = this.userService.getUser(user.getId());

                        Arrays.stream(text.split("\n"))
                            .map(s -> s.split(" - "))
                            .map(s -> new Word(s[0], s[1]))
                            .forEach(englishUser.getWords()::add);
                    }
                }
            }
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
}
