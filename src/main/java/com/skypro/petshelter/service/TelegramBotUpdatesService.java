package com.skypro.petshelter.service;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.petshelter.entity.User;
import com.skypro.petshelter.repositories.UserRepository;
import com.skypro.petshelter.repositories.VolunteerRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TelegramBotUpdatesService {

    private final UserRepository userRepository;
    private final VolunteersService volunteersService;

    public TelegramBotUpdatesService(UserRepository userRepository, VolunteerRepository volunteerRepository, VolunteersService volunteersService) {
        this.userRepository = userRepository;
        this.volunteersService = volunteersService;
    }

    @Transactional
    public void addUser(Long chatId, String name) {
        User user = new User();
        user.setChatId(chatId);
        user.setName(name);
        userRepository.save(user);
    }

    public SendMessage start(Long chatId, String userName) {
        if (!userRepository.existsById(chatId)) {
            addUser(chatId, userName);
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
        return new SendMessage(chatId, "Чем я могу помочь?")
                .replyMarkup(new ReplyKeyboardMarkup(new String[][]{
                        {"Форма ежедневного отчета", "Позвать волонтера"}
                }).resizeKeyboard(true));
    }

    public void call(String userName, Long chatId){
            String contacts = userRepository.findById(chatId).orElseThrow().getContact();
            volunteersService.call(userName, contacts);
        }


}
