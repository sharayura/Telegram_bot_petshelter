package com.skypro.petshelter.service;

import com.skypro.petshelter.entity.User;
import com.skypro.petshelter.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    @Transactional
    public User moveDog(Long chatId, String dogName) {
        User user = userRepository.findById(chatId).orElseThrow();
        user.setDogName(dogName);
        user.setDaysTrial(30);
        user.setFailsInRow(0);
        if (dogName == null) {
            user.setDaysTrial(null);
        }
        return user;
    }

    @Transactional
    public User setTrialDays(Long chatId, Integer daysTrial) {
        User user = userRepository.findById(chatId).orElseThrow();
        user.setDaysTrial(daysTrial);
        return user;
    }

    @Transactional
    public User setContacts(Long chatId, String contact) {
        User user = userRepository.findById(chatId).orElseThrow();
        user.setContact(contact);
        return user;
    }
}
