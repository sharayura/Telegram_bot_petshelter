package com.skypro.petshelter.controller;

import com.skypro.petshelter.entity.Report;
import com.skypro.petshelter.repositories.ReportRepository;
import com.skypro.petshelter.service.ReportService;
import com.skypro.petshelter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportControllerTest {

    private final Long chatIdTest = 1111L;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    ReportService reportService;

    @Autowired
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void reportAPITest() throws Exception {
        mockMvc.perform(
                        get("/report/chat-id").param("chatId", chatIdTest.toString()))
                .andExpect(status().isNotFound());

        File photoTest = new File("src/main/resources/route.jpg");
        byte[] photoTestBytes = null;
        try {
            photoTestBytes= Files.readAllBytes(photoTest.getAbsoluteFile().toPath());
        } catch (IOException e) {
        }
        Report report = reportService.savePhoto(chatIdTest, photoTest);
        reportRepository.save(report);
        userService.addUser(chatIdTest, "name");

        mockMvc.perform(
                        get("/report/chat-id").param("chatId", chatIdTest.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dateChatId").value(reportService.parseDateChatId(chatIdTest, LocalDate.now())));

        mockMvc.perform(
                        get("/report/date").param("date", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dateChatId").value(reportService.parseDateChatId(chatIdTest, LocalDate.now())));

        mockMvc.perform(
                        get("/report/photo").param("chatId", chatIdTest.toString())
                                .param("date", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(content().bytes(photoTestBytes));
    }

}

