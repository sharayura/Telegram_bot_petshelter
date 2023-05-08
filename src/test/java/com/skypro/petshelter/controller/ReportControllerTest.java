package com.skypro.petshelter.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportControllerTest {

    private final Long chatIdTest = 1111L;


    @Autowired
    MockMvc mockMvc;

    @Test
    void reportAPITest() throws Exception {
        mockMvc.perform(
                        get("/report/chat-id").param("chatId", chatIdTest.toString()))
                .andExpect(status().isNotFound());
    }

}

