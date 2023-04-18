package com.skypro.petshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.petshelter.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class TelegramBotUpdatesService {

    private final UserService userService;
    private final VolunteersService volunteersService;
    private final UserRepository userRepository;
    private final TelegramBot telegramBot;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesService.class);

    public TelegramBotUpdatesService(UserService userService, VolunteersService volunteersService, UserRepository userRepository, TelegramBot telegramBot) {
        this.userService = userService;
        this.volunteersService = volunteersService;
        this.userRepository = userRepository;
        this.telegramBot = telegramBot;
    }

    public SendMessage start(Long chatId, String userName) {
        if (!userRepository.existsById(chatId)) {
            userService.addUser(chatId, userName);
        }
        return new SendMessage(chatId, userName + ", привет!")
                .replyMarkup(new ReplyKeyboardMarkup(new String[][]{
                        {"Информация о приюте", "Как взять собаку"},
                        {"Прислать отчет", "Позвать волонтера"}
                }).resizeKeyboard(true));
    }

    public SendMessage stage1(Long chatId) {
        return new SendMessage(chatId, "Чем я могу помочь?")
                .replyMarkup(new ReplyKeyboardMarkup(new String[][]{
                        {"О приюте", "Как нас найти"},
                        {"Техника безопасности на территории", "Как взять собаку"},
                        {"Оставьте ваши контактные данные", "Позвать волонтера"}
                }).resizeKeyboard(true));
    }

    public SendMessage stage2(Long chatId) {
        return new SendMessage(chatId, "Чем я могу помочь?")
                .replyMarkup(new ReplyKeyboardMarkup(new String[][]{
                        {"Правила знакомства с собакой", "Список необходимых документов"},
                        {"Рекомендации по транспортировке", "Обустройство дома щенка"},
                        {"Если у собаки ограничены возможности", "Советы кинолога по первичному общению"},
                        {"Кинологи, которым мы доверяем", "Почему мы можем отказать"},
                        {"Оставьте ваши контактные данные", "Позвать волонтера"}
                }).resizeKeyboard(true));
    }

    public SendMessage stage3(Long chatId) {
        String reply = "Чем я могу помочь?";
        if (userRepository.findById(chatId).orElseThrow().getDaysTrial() != null) {
            reply = """
                    Для отправки отчета выберите тип информации:
                    /photo - фотография животного,
                    /ration - сегодняшний рацион,
                    /health - общее самочувствие и привыкание к новому месту,
                    /habits - изменение в поведении: отказ от старых привычек, приобретение новых.
                    Повторная отправка затрет отправленную ранее сегодняшнюю информацию соответствующего типа.
                    """;
        }
        return new SendMessage(chatId, reply)
                .replyMarkup(new ReplyKeyboardMarkup(new String[][]{
                        {"Форма ежедневного отчета", "Позвать волонтера"}
                }).resizeKeyboard(true));
    }

    public void call(String userName, Long chatId) {
        String contacts = userRepository.findById(chatId).orElseThrow().getContact();
        volunteersService.callVolunteer(userName, contacts, "");
    }

    public File getFileFromPhotoSizeArray(PhotoSize[] photoArray ) {
        PhotoSize photo = photoArray[photoArray.length - 1];  // Берем самый лучший по качеству файл
        String url = "https://api.telegram.org/file/bot"
                + telegramBot.getToken() + "/"
                + telegramBot.execute(new GetFile(photo.fileId())).file().filePath();

        File file = new File("src/main/resources/temp.jpg");
        try {
            InputStream in = new URL(url).openStream();
            Files.copy(in, Paths.get(file.getPath()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return file;
    }

}
