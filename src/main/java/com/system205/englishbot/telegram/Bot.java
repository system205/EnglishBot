package com.system205.englishbot.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {
    private final Long ownerId;

    public Bot(@Value("${bot.token}") String botToken, @Value("${bot.owner-id}") Long ownerId) {
        super(botToken);
        this.ownerId = ownerId;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("{}", update);
    }

    @Override
    public String getBotUsername() {
        return "EnglishBot";
    }
}
