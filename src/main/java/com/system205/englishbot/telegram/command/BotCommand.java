package com.system205.englishbot.telegram.command;

import com.system205.englishbot.telegram.TelegramBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotCommand {
    void execute(Update update);

    BotCommand DoNothingCommand = new DoNothingCommand();

    default void execute(Update update, TelegramBot bot) {
        execute(update);
    }

    final class DoNothingCommand implements BotCommand {
        private DoNothingCommand(){}
        @Override
        public void execute(Update update) { /* nothing to do */ }
    }
}
