package com.skypro.petshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.petshelter.service.TelegramBotUpdatesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class TelegramBotUpdatesListenerTest {

    private final Long chatIdTest = 1111L;
    private final String nameTest = "Test name";


    @Autowired
    TelegramBotUpdatesListener telegramBotUpdatesListener;

    @Autowired
    TelegramBotUpdatesService telegramBotUpdatesService;

    @MockBean
    TelegramBot telegramBot;

    @Test
    void updatesListenerStartTest() {
        SendMessage actual = actualMessage("/start");
        Assertions.assertEquals(actual.getParameters().get("text"), telegramBotUpdatesService.start(chatIdTest, nameTest).getParameters().get("text"));
        Assertions.assertNotNull(actual.getParameters().get("reply_markup"));
    }

    @Test
    void updatesListenerTakeTest() {
        SendMessage actual = actualMessage("Как взять собаку");
        Assertions.assertEquals(actual.getParameters().get("text"), telegramBotUpdatesService.stage2(chatIdTest).getParameters().get("text"));
        Assertions.assertNotNull(actual.getParameters().get("reply_markup"));
    }

    private SendMessage actualMessage(String text) {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);
        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(chatIdTest);
        when(chat.firstName()).thenReturn(nameTest);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);

        when(message.text()).thenReturn(text);
        telegramBotUpdatesListener.process(List.of(update));
        verify(telegramBot).execute(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }
}
