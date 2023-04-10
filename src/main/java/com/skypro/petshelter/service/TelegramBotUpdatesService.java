package com.skypro.petshelter.service;

import com.skypro.petshelter.entity.User;
import com.skypro.petshelter.repositories.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TelegramBotUpdatesService {

    private final UserRepository userRepository;

    public TelegramBotUpdatesService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void addUser(Long chatId, String name) {
        User user = new User();
        user.setChatId(chatId);
        user.setName(name);
        user.setStage(0);
        userRepository.save(user);
    }
}
