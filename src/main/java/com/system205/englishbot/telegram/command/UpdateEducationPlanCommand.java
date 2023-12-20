package com.system205.englishbot.telegram.command;

import com.system205.englishbot.entity.EnglishUser;
import com.system205.englishbot.services.EducationService;
import com.system205.englishbot.services.UserService;
import com.system205.englishbot.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component("/update_education_plan")
@RequiredArgsConstructor
public class UpdateEducationPlanCommand implements  BotCommand{
    private final UserService userService;
    private final EducationService educationService;
    @Override
    public void execute(Update update) {
        final EnglishUser user = userService.getUser(update.getMessage().getFrom().getId());
        educationService.getNewEducationPlanForUser(user);
    }

    @Override
    public void execute(Update update, TelegramBot bot) {
        BotCommand.super.execute(update, bot);
    }
}
