package com.system205.englishbot.telegram.command;


import com.system205.englishbot.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component("/start")
@Slf4j
public class StartCommand implements BotCommand{
    @Value("${bot.owner-id}")
    private Long ownerId;
    @Override
    public void execute(Update update) { /* deprecated by new method call with the bot as argument */}

    @Override
    public void execute(Update update, TelegramBot bot) {
        final User user = update.getMessage().getFrom();
        log.info("User#{} sent /start", user.getId());
        bot.sendMessage(ownerId, "%s#%s sent /start".formatted(user.getUserName(), user.getId()));
    }
}
