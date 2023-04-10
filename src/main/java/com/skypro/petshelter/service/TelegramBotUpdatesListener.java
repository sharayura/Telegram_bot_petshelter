package com.skypro.petshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.petshelter.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final UserRepository userRepository;
    private final TelegramBotUpdatesService telegramBotUpdatesService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      UserRepository userRepository,
                                      TelegramBotUpdatesService telegramBotUpdatesService) {
        this.telegramBot = telegramBot;
        this.userRepository = userRepository;
        this.telegramBotUpdatesService = telegramBotUpdatesService;
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

                if ("/start".equals(text) && !userRepository.existsById(chatId)) {
                    String name = message.chat().firstName();
                    telegramBotUpdatesService.addUser(chatId, name);
                    SendMessage menu = new SendMessage(chatId, name + ", привет!")
                            .replyMarkup(new ReplyKeyboardMarkup(new String[][]{
                                    {"Информация о приюте", "Как взять собаку"},
                                    {"Прислать отчет", "Позвать волонтера"}
                            }).resizeKeyboard(true));
                    telegramBot.execute(menu);
                }
                if ("Информация о приюте".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            """
                                    Я телеграм-бот приюта животных из Астаны, могу дать информацию о приюте
                                    и о том, как взять себе животное из приюта. 
                                     """);
                    telegramBot.execute(reply);
                }

            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
