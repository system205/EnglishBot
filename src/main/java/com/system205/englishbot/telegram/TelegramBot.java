package com.system205.englishbot.telegram;

public interface TelegramBot {
    void sendMessage(Long chatId, String text);
}
