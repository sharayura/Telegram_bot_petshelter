package com.skypro.petshelter.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VolunteerControllerTest {

    private final Long chatIdTest = 1111L;
    private final String nameTest = "Test name";
    private final String contactTest = "Test contact";


    @Autowired
    MockMvc mockMvc;

    @Test
    void volunteerAPITest() throws Exception {
        mockMvc.perform(
                        post("/volunteer").param("chatId", chatIdTest.toString())
                                .param("name", nameTest)
                                .param("contact", contactTest))
                .andExpect(status().isOk());
        mockMvc.perform(
                        delete("/volunteer").param("chatId", chatIdTest.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(nameTest));
        mockMvc.perform(
                        delete("/volunteer").param("chatId", chatIdTest.toString()))
                .andExpect(status().isNotFound());
    }

}
