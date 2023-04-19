package com.skypro.petshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBotUpdatesService telegramBotUpdatesService;
    private final UserService userService;
    private final ReportService reportService;
    private String lastCommand;
    private static final Pattern CONTACT_PATTERN = Pattern.compile("^\\d{11}$");


    public TelegramBotUpdatesListener(TelegramBot telegramBot,
                                      TelegramBotUpdatesService telegramBotUpdatesService, UserService userService, ReportService reportService) {
        this.telegramBot = telegramBot;
        this.telegramBotUpdatesService = telegramBotUpdatesService;
        this.userService = userService;
        this.reportService = reportService;
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

                if ("О приюте".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            """
                                    Наш приют самый лучший в Астане!
                                    Мы заботимся о животных как на территории, так и после усыновления.
                                    Более 300 счастливых животных обрели счастливых хозяев!
                                    """);
                    telegramBot.execute(reply);
                }

                if ("Как нас найти".equals(text)) {
                    telegramBot.execute(new SendPhoto(chatId, new File("src/main/resources/route.jpg"))
                            .caption("Три дня полем, три дня лесом."));
                }

                if ("Техника безопасности на территории".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            "Бот может выдать общие рекомендации о технике безопасности на территории приюта.");
                    telegramBot.execute(reply);
                }

                if ("Правила знакомства с собакой".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            "Бот может выдать правила знакомства с собакой до того, как можно забрать ее из приюта.");
                    telegramBot.execute(reply);
                }

                if ("Список необходимых документов".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            "Бот может выдать список документов, необходимых для того, чтобы взять собаку из приюта.");
                    telegramBot.execute(reply);
                }

                if ("Рекомендации по транспортировке".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            "Бот может выдать список рекомендаций по транспортировке животного.");
                    telegramBot.execute(reply);
                }

                if ("Обустройство дома щенка".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            "Бот может выдать список рекомендаций по обустройству дома для щенка.");
                    telegramBot.execute(reply);
                }

                if ("Если у собаки ограничены возможности".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            "Бот может выдать список рекомендаций по обустройству дома для собаки с ограниченными " +
                                    "возможностями (зрение, передвижение).");
                    telegramBot.execute(reply);
                }

                if ("Советы кинолога по первичному общению".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            "Бот может выдать советы кинолога по первичному общению с собакой.");
                    telegramBot.execute(reply);
                }

                if ("Кинологи, которым мы доверяем".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            "Бот может выдать рекомендации по проверенным кинологам для дальнейшего обращения к ним.");
                    telegramBot.execute(reply);
                }

                if ("Почему мы можем отказать".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            "Бот может выдать список причин, почему могут отказать и не дать забрать собаку из приюта. ");
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

                if ("/photo".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            "В следующем сообщении прикрепите одно фото, отправьте со сжатием!");
                    telegramBot.execute(reply);
                    lastCommand = "/photo";
                    return;
                }

                if ("/photo".equals(lastCommand) && update.message().photo() != null) {
                    lastCommand = null;
                    PhotoSize[] photoArray = update.message().photo();
                    File file = telegramBotUpdatesService.getFileFromPhotoSizeArray(photoArray);
                    reportService.savePhoto(chatId, file);
                }

                if ("/ration".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            "В следующем сообщении опишите сегодняшний рацион собаки");
                    telegramBot.execute(reply);
                    lastCommand = "/ration";
                    return;
                }

                if ("/ration".equals(lastCommand) && text != null) {
                    lastCommand = null;
                    reportService.saveRation(chatId, text);
                }

                if ("/health".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            "В следующем сообщении опишите общее самочувствие и привыкание к новому месту");
                    telegramBot.execute(reply);
                    lastCommand = "/health";
                    return;
                }

                if ("/health".equals(lastCommand) && text != null) {
                    lastCommand = null;
                    reportService.saveHealth(chatId, text);
                }

                if ("/habits".equals(text)) {
                    SendMessage reply = new SendMessage(chatId,
                            "В следующем сообщении опишите изменение в поведении: " +
                                    "отказ от старых привычек, приобретение новых");
                    telegramBot.execute(reply);
                    lastCommand = "/habits";
                    return;
                }

                if ("/habits".equals(lastCommand) && text != null) {
                    lastCommand = null;
                    reportService.saveHabits(chatId, text);
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
