package com.skypro.petshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import liquibase.pro.packaged.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBotUpdatesService telegramBotUpdatesService;
    private final VolunteersService volunteersService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      TelegramBotUpdatesService telegramBotUpdatesService, VolunteersService volunteersService) {
        this.telegramBot = telegramBot;
        this.telegramBotUpdatesService = telegramBotUpdatesService;
        this.volunteersService = volunteersService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Update: {}", update);
                Message message = update.message();
                Long chatId = message.chat().id();
                String text = message.text();
                String userName = message.chat().username();
                String firstName = message.chat().username();

                if ("/start".equals(text)) {
                    telegramBot.execute(telegramBotUpdatesService.start(chatId, firstName));
                }

                if ("Информация о приюте".equals(text) || "/info".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            """
                                    Я телеграм-бот приюта животных из Астаны! 
                                    Могу дать информацию о приюте и о том, как взять себе животное из приюта. 
                                    """);
                    telegramBot.execute(reply);
                    telegramBot.execute(telegramBotUpdatesService.stage1(chatId));
                }

                if ("Как взять собаку".equals(text) || "/take".equals(text)) {
                    telegramBot.execute(telegramBotUpdatesService.stage2(chatId));
                }

                if ("Прислать отчет".equals(text) || "/report".equals(text)) {
                    telegramBot.execute(telegramBotUpdatesService.stage3(chatId));
                }

                if ("Позвать волонтера".equals(text) || "/call".equals(text)) {
                    SendMessage reply = new SendMessage(chatId, firstName
                            + ", спасибо за обращение, наши волонтеры скоро свяжутся с Вами!");
                    telegramBot.execute(reply);
                    telegramBotUpdatesService.call(userName, chatId);
                }

            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
