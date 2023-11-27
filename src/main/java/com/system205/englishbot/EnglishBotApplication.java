package com.system205.englishbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@Slf4j
public class EnglishBotApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(EnglishBotApplication.class, args);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            LongPollingBot bot = context.getBean(LongPollingBot.class);
            botsApi.registerBot(bot);
            log.info("Bot is registered successfully");
        } catch (TelegramApiException e) {
            log.error("Can't register a bot. ", e);
        }
    }

}
