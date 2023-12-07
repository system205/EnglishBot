package com.system205.englishbot.telegram.command;


import com.system205.englishbot.entity.EnglishUser;
import com.system205.englishbot.services.UserService;
import com.system205.englishbot.telegram.Bot;
import com.system205.englishbot.telegram.TelegramBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component("/info")
public class InfoCommand implements BotCommand {
    private final TelegramBot bot;
    private final UserService userService;

    @Lazy
    public InfoCommand(Bot bot, UserService userService) {
        this.bot = bot;
        this.userService = userService;
    }


    @Override
    public void execute(Update update) {
        log.trace("Execute info command");

        final User user = update.getMessage().getFrom();

        final EnglishUser englishUser = userService.getUser(user.getId());
        final Instant now = Instant.now();
        String info = """
            Last notification was %s minutes ago
            You interval between notifications is %s minutes
            You saved %d words
            Number of daily words: %d""".formatted(
            Duration.between(englishUser.getLastNotified(), now).toMinutes(),
            englishUser.getInterval().toMinutes(),
            userService.getNumberOfWords(englishUser.getId()),
            englishUser.getEducationPlan().getNumberOfWords()
        );
        bot.sendMessage(user.getId(), info);
    }
}
