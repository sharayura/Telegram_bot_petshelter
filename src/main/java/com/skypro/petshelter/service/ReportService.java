package com.skypro.petshelter.service;

import com.skypro.petshelter.entity.Report;
import com.skypro.petshelter.repositories.ReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final Logger logger = LoggerFactory.getLogger(ReportService.class);

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<Report> getReportsByChatId(Long chatId) {
        return reportRepository.findReportsByChatId(chatId);
    }

    public List<Report> getReportsByDate(LocalDate date) {
        return reportRepository.findReportsByDate(date);
    }

    public String parseDateChatId(Long chatId, LocalDate date) {
        return date.toString() + '-' + chatId;
    }

    public Boolean isReportPresent(Long chatId, LocalDate date) {
        return reportRepository.findById(parseDateChatId(chatId, date)).isPresent();
    }

    public Report getReport(Long chatId, LocalDate date) {
        return reportRepository.findById(parseDateChatId(chatId, date)).orElse(null);
    }

    public boolean hasReportAnyNull(Report report) {
        return report.getHabits() == null || report.getRation() == null ||
                report.getPhoto() == null || report.getHealth() == null;
    }

    public String replyToReport(Report report) {
        String message = "Спасибо!";
        if (report.getPhoto() == null) {
            message = message + " Отправьте сегодняшнее фото животного /photo .";
        }
        if (report.getRation() == null) {
            message = message + " Опишите сегодняшний рацион животного /ration .";
        }
        if (report.getHealth() == null) {
            message = message + " Опишите сегодняшнее самочувствие животного /health .";
        }
        if (report.getHabits() == null) {
            message = message + " Опишите сегодняшние изменения в поведении животного /habits .";
        }
        return message;
    }


    @Transactional
    public Report createReport(Long chatId) {
        if (!isReportPresent(chatId, LocalDate.now())) {
            Report report = new Report();
            report.setDateChatId(parseDateChatId(chatId, LocalDate.now()));
            report.setChatId(chatId);
            report.setDate(LocalDate.now());
            reportRepository.save(report);
        }
        return reportRepository.findById(parseDateChatId(chatId, LocalDate.now())).orElseThrow();
    }

    @Transactional
    public Report savePhoto(Long chatId, File file) {
       Report report = createReport(chatId);
        byte[] data = null;
        try {
            data = Files.readAllBytes(file.getAbsoluteFile().toPath());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        report.setPhoto(data);
        return report;
    }

    @Transactional
    public Report  saveRation(Long chatId, String text) {
        Report report = createReport(chatId);
        report.setRation(text);
        return report;
    }

    @Transactional
    public Report saveHealth(Long chatId, String text) {
        Report report = createReport(chatId);
        report.setHealth(text);
        return report;
    }

    @Transactional
    public Report saveHabits(Long chatId, String text) {
        Report report = createReport(chatId);
        report.setHabits(text);
        return report;
    }

}
