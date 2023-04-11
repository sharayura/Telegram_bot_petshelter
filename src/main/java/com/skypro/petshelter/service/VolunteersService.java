package com.skypro.petshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.petshelter.repositories.VolunteerRepository;
import org.springframework.stereotype.Service;

@Service
public class VolunteersService {

    private final VolunteerRepository volunteerRepository;
    private final TelegramBot telegramBot;

    public VolunteersService(VolunteerRepository volunteerRepository, TelegramBot telegramBot) {
        this.volunteerRepository = volunteerRepository;
        this.telegramBot = telegramBot;
    }


    public void call(String userName) {
        volunteerRepository.findAll().forEach(volunteer -> {
            SendMessage message = new SendMessage(volunteer.getChatId(),
                    "Пожалуйста, свяжитесь с @" + userName);
            telegramBot.execute(message);

        });

    }
}
