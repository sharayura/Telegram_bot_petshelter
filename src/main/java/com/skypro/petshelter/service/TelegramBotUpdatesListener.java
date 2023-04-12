package com.skypro.petshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBotUpdatesService telegramBotUpdatesService;
    private final UserService userService;
    private String lastCommand;
    private static final Pattern CONTACT_PATTERN = Pattern.compile("^\\d{11}$");


    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      TelegramBotUpdatesService telegramBotUpdatesService, UserService userService) {
        this.telegramBot = telegramBot;
        this.telegramBotUpdatesService = telegramBotUpdatesService;
        this.userService = userService;
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
                String firstName = message.chat().firstName();
                firstName = (firstName == null) ? "User" : firstName;

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
                    SendMessage reply;
                    if (userService.findUser(chatId).getContact() == null
                            || userService.findUser(chatId).getContact().isBlank()) {
                        reply = new SendMessage(chatId,
                                "Сначала оставьте, пожалуйста, контактные данные -->  /contact");
                    } else {
                        reply = new SendMessage(chatId,
                                "Спасибо за обращение, наши волонтеры скоро свяжутся с Вами!");
                        telegramBotUpdatesService.call(userName, chatId);
                    }
                    telegramBot.execute(reply);
                }

                if ("/contact".equals(text) || "Оставьте ваши контактные данные".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            "Напишите номер телефона в международном формате (только цифры)");
                    telegramBot.execute(reply);
                    lastCommand = "/contact";
                    return;
                }

                if ("/contact".equals(lastCommand) && text != null) {
                    lastCommand = null;
                    SendMessage reply;
                    if (CONTACT_PATTERN.matcher(text).matches()) {
                        userService.setContacts(chatId, text);
                        reply = new SendMessage(chatId,
                                "Ваш номер внесен в базу");
                    } else {
                        reply = new SendMessage(chatId,
                                "Ваш номер не внесен в базу, неверный формат");
                    }
                    telegramBot.execute(reply);

                }

            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
