package com.system205.englishbot.telegram.command;

import com.system205.englishbot.entity.EnglishUser;
import com.system205.englishbot.services.UserService;
import com.system205.englishbot.telegram.Bot;
import com.system205.englishbot.telegram.TelegramBot;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@Component("/set_daily_words")
public class SetDailyWordsCommand implements BotCommand{
    private final TelegramBot bot;
    private final UserService userService;

    @Lazy
    public SetDailyWordsCommand(Bot bot, UserService userService) {
        this.bot = bot;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) {
        final Message message = update.getMessage();
        final User user = message.getFrom();
        int numberOfDailyWords = 0;
        try {
            final String number = message.getText().split(" ")[1];
            numberOfDailyWords = Integer.parseInt(number);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            bot.sendMessage(user.getId(), "Please specify a number: /set_daily_words 3");
        }

        // Update number of words
        final EnglishUser englishUser = userService.getUser(user.getId());
        englishUser.getEducationPlan().setNumberOfWords(numberOfDailyWords);
        userService.updateUsers(List.of(englishUser));
    }
}
