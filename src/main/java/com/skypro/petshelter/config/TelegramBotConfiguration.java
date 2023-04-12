package com.skypro.petshelter.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import com.pengrad.telegrambot.model.botcommandscope.BotCommandScopeDefault;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetMyCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class TelegramBotConfiguration {

    @Bean
    public TelegramBot telegramBot(@Value("${telegram.bot.token}") String token) {
        TelegramBot bot = new TelegramBot(token);
        bot.execute(new DeleteMyCommands());
        bot.execute(new SetMyCommands(
                new BotCommand("/start", "Начнем общение"),
                new BotCommand("/info", "Узнать информацию о приюте"),
                new BotCommand("/take", "Как взять собаку из приюта"),
                new BotCommand("/report", "Прислать отчет о питомце"),
                new BotCommand("/contact", "Ваши контактные данные"),
                new BotCommand("/call", "Позвать волонтера")));
        return bot;
    }
}
