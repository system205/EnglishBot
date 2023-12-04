package com.system205.englishbot.telegram.command;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface BotCommand {
    void execute(Update update);

    BotCommand DoNothingCommand = new DoNothingCommand();
    final class DoNothingCommand implements BotCommand {
        private DoNothingCommand(){}
        @Override
        public void execute(Update update) { /* nothing to do */ }
    }
}
