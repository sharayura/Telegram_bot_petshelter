package com.skypro.petshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.petshelter.entity.Volunteer;
import com.skypro.petshelter.repositories.VolunteerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VolunteersServiceTest {
    @Mock
    private VolunteerRepository volunteerRepository;

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private VolunteersService volunteersService;

    @Test
    void testAddVolunteer() {
        Long chatId = 123L;
        String name = "John Doe";
        String contact = "john.doe@example.com";

        Volunteer volunteer = new Volunteer();
        volunteer.setChatId(chatId);
        volunteer.setName(name);
        volunteer.setContact(contact);

        Mockito.when(volunteerRepository.save(Mockito.any(Volunteer.class))).thenReturn(volunteer);

        Volunteer result = volunteersService.addVolunteer(chatId, name, contact);

        Mockito.verify(volunteerRepository).save(Mockito.any(Volunteer.class));
        Assertions.assertEquals(volunteer, result);
    }

    @Test
    void testDeleteVolunteer() {
        Long chatId = 123L;

        Volunteer volunteer = new Volunteer();
        volunteer.setChatId(chatId);
        volunteer.setName("John Doe");
        volunteer.setContact("john.doe@example.com");

        Mockito.when(volunteerRepository.findById(chatId)).thenReturn(Optional.of(volunteer));

        Volunteer result = volunteersService.deleteVolunteer(chatId);

        Mockito.verify(volunteerRepository).deleteById(chatId);
        Assertions.assertEquals(volunteer, result);
    }

    @Test
    void testDeleteVolunteerWithNonexistentId() {
        Long chatId = 123L;

        Mockito.when(volunteerRepository.findById(chatId)).thenReturn(Optional.empty());

        Volunteer result = volunteersService.deleteVolunteer(chatId);

        Mockito.verify(volunteerRepository, Mockito.never()).deleteById(chatId);
        Assertions.assertNull(result);
    }

    @Test
    public void testCallVolunteer() {
        Volunteer volunteer1 = new Volunteer();
        volunteer1.setChatId(1111L);
        volunteer1.setName("Volunteer 1");
        volunteer1.setContact("Contact 1");

        Volunteer volunteer2 = new Volunteer();
        volunteer2.setChatId(2222L);
        volunteer2.setName("Volunteer 2");
        volunteer2.setContact("Contact 2");

        when(volunteerRepository.findAll()).thenReturn(Arrays.asList(volunteer1, volunteer2));

        volunteersService.callVolunteer("User 1", "Contact 3", "Alert");

        verify(volunteerRepository).findAll();

        SendMessage message1 = new SendMessage(1111L, "Пожалуйста, свяжитесь с User 1. Контактные данные: Contact 3. Alert");
        SendMessage message2 = new SendMessage(2222L, "Пожалуйста, свяжитесь с User 1. Контактные данные: Contact 3. Alert");

        verify(telegramBot).execute(message1);
        verify(telegramBot).execute(message2);
    }

    @Test
    public void testCallVolunteerNoVolunteers() {
        when(volunteerRepository.findAll())
                .thenReturn(Collections.emptyList());

        volunteersService.callVolunteer("John", "123-456-7890", "Please help");

        verify(telegramBot, org.mockito.Mockito.never()).execute(any());
    }
}
