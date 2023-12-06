package com.system205.englishbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Slf4j
@Import(value = org.telegram.telegrambots.starter.TelegramBotStarterConfiguration.class)
public class EnglishBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnglishBotApplication.class, args);
    }

}
