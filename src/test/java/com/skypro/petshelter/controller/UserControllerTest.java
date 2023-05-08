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
public class UserControllerTest {
    private final Long chatIdTest = 1111L;
    private final String nameTest = "Test name";
    private final String contactTest = "Test contact";
    private final String dogNameTest = "Bobik";
    private final Integer daysTrialTest = 20;


    @Autowired
    MockMvc mockMvc;

    @Test
    void userAPITest() throws Exception {
        mockMvc.perform(
                        post("/user").param("chatId", chatIdTest.toString())
                                .param("name", nameTest)
                                .param("contact", contactTest))
                .andExpect(status().isOk());

        mockMvc.perform(
                        put("/user/give-dog").param("chatId", chatIdTest.toString())
                                .param("dogName", dogNameTest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dogName").value(dogNameTest));

        mockMvc.perform(
                        put("/user/set-trial").param("chatId", chatIdTest.toString())
                                .param("daysTrial", daysTrialTest.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.daysTrial").value(daysTrialTest.toString()));

        mockMvc.perform(
                        put("/user/deprive-dog").param("chatId", chatIdTest.toString())
                                .param("dogName", dogNameTest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dogName").isEmpty());

        mockMvc.perform(
                        delete("/user").param("chatId", chatIdTest.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(nameTest));

        mockMvc.perform(
                        delete("/user").param("chatId", chatIdTest.toString()))
                .andExpect(status().isNotFound());
    }
}
