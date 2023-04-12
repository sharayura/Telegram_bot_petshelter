package com.skypro.petshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.petshelter.entity.Volunteer;
import com.skypro.petshelter.repositories.VolunteerRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class VolunteersService {

    private final VolunteerRepository volunteerRepository;
    private final TelegramBot telegramBot;

    public VolunteersService(VolunteerRepository volunteerRepository, TelegramBot telegramBot) {
        this.volunteerRepository = volunteerRepository;
        this.telegramBot = telegramBot;
    }
    @Transactional
    public Volunteer addVolunteer(Long chatId, String name, String contact) {
        Volunteer volunteer = new Volunteer();
        volunteer.setChatId(chatId);
        volunteer.setName(name);
        volunteer.setContact(contact);
        return volunteerRepository.save(volunteer);
    }

    @Transactional
    public Volunteer deleteVolunteer(Long chatId) {
        Volunteer volunteer = volunteerRepository.findById(chatId).orElse(null);
        if (volunteer == null) {
            return null;
        }
        volunteerRepository.deleteById(chatId);
        return volunteer;
    }

    public void call(String userName, String contacts) {
        volunteerRepository.findAll().forEach(volunteer -> {
            SendMessage message = new SendMessage(volunteer.getChatId(),
                    "Пожалуйста, свяжитесь с @" + userName + ". Контактные данные: " + contacts);
            telegramBot.execute(message);

        });

    }
}
