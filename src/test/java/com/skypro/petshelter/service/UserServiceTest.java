package com.skypro.petshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.petshelter.entity.User;
import com.skypro.petshelter.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testAddUser() {
        Long chatId = 1L;
        String name = "TestName";
        User expectedUser = new User();
        expectedUser.setChatId(chatId);
        expectedUser.setName(name);

        when(userRepository.save(any())).thenReturn(expectedUser);

        User actualUser = userService.addUser(chatId, name);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testDeleteUser() {
        Long chatId = 1L;
        User expectedUser = new User();
        expectedUser.setChatId(chatId);

        when(userRepository.findById(chatId)).thenReturn(Optional.of(expectedUser));
        doNothing().when(userRepository).deleteById(chatId);

        User actualUser = userService.deleteUser(chatId);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testFindUser() {
        Long chatId = 1L;
        User expectedUser = new User();
        expectedUser.setChatId(chatId);

        when(userRepository.findById(chatId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.findUser(chatId);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void testMoveDogWithDogName() {
        Long chatId = 123456L;
        String dogName = "Rex";
        User expectedUser = new User();
        expectedUser.setChatId(chatId);
        expectedUser.setDogName(dogName);
        expectedUser.setDaysTrial(30);
        expectedUser.setFailsInRow(0);
        when(userRepository.findById(chatId)).thenReturn(Optional.of(expectedUser));
        User actualUser = userService.moveDog(chatId, dogName);
        assertEquals(expectedUser, actualUser);
        verifyNoInteractions(telegramBot);
    }

    @Test
    public void testMoveDogWhenDogNameIsNull() {
        Long chatId = 123456L;
        User expectedUser = new User();
        expectedUser.setChatId(chatId);
        expectedUser.setDaysTrial(null);
        when(userRepository.findById(chatId)).thenReturn(Optional.of(expectedUser));
        User actualUser = userService.moveDog(chatId, null);
        assertEquals(expectedUser, actualUser);
        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    public void testSetTrialDays() {
        Long chatId = 123456L;
        Integer daysTrial = 15;
        User expectedUser = new User();
        expectedUser.setChatId(chatId);
        expectedUser.setDaysTrial(daysTrial);
        when(userRepository.findById(chatId)).thenReturn(Optional.of(expectedUser));
        User actualUser = userService.setTrialDays(chatId, daysTrial);
        assertEquals(expectedUser, actualUser);
        verify(telegramBot).execute(any(SendMessage.class));
    }

    @Test
    void testSetContacts() {
        Long chatId = 123L;
        String contact = "test@test.com";
        User user = new User();
        user.setChatId(chatId);
        when(userRepository.findById(chatId)).thenReturn(Optional.of(user));

        User result = userService.setContacts(chatId, contact);

        assertNotNull(result);
        assertEquals(contact, result.getContact());
    }
}









