package com.skypro.petshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.petshelter.entity.User;
import com.skypro.petshelter.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Класс-сервис работы с сущностью пользователь
 * @author Sharapov Yuri
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    public final TelegramBot telegramBot;

    public UserService(UserRepository userRepository, TelegramBot telegramBot) {
        this.userRepository = userRepository;
        this.telegramBot = telegramBot;
    }


    @Transactional
    public User addUser(Long chatId, String name) {
        User user = new User();
        user.setChatId(chatId);
        user.setName(name);
        return userRepository.save(user);
    }

    @Transactional
    public User deleteUser(Long chatId) {
        User user = userRepository.findById(chatId).orElse(null);
        if (user == null) {
            return null;
        }
        userRepository.deleteById(chatId);
        return user;
    }

    public User findUser(Long chatId) {
        return userRepository.findById(chatId).orElse(null);
    }

    /**
     * Метод назначения пользователя хозяином или лишения его собаки.
     * Если передается кличка собаки ({@code dogName}), то пользователь становится ее хозяином.
     * Если передается <u>null</u> ({@code dogName == null}), то пользователь лишается собаки.
     * @throws java.util.NoSuchElementException - пользователь не найден
     * @param chatId идентификатор чата пользователя
     * @param dogName кличка собаки
     * @return {@link User}
     */
    @Transactional
    public User moveDog(Long chatId, String dogName) {
        User user = userRepository.findById(chatId).orElseThrow();
        user.setDogName(dogName);
        user.setDaysTrial(30);
        user.setFailsInRow(0);
        if (dogName == null) {
            user.setDaysTrial(null);
            SendMessage message = new SendMessage(chatId,
                    "К сожалению, Вы не прошли испытательный срок, верните бедное животное!!!");
            telegramBot.execute(message);
        }
        return user;
    }

    @Transactional
    public User setTrialDays(Long chatId, Integer daysTrial) {
        User user = userRepository.findById(chatId).orElseThrow();
        user.setDaysTrial(daysTrial);
        SendMessage message = new SendMessage(chatId,
                "Ваш испытательный срок изменен, осталось еще " + daysTrial + " дней.");
        telegramBot.execute(message);
        return user;
    }

    @Transactional
    public User setContacts(Long chatId, String contact) {
        User user = userRepository.findById(chatId).orElseThrow();
        user.setContact(contact);
        return user;
    }
}
