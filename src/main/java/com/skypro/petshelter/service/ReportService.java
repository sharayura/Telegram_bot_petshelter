package com.skypro.petshelter.service;

import com.skypro.petshelter.entity.Report;
import com.skypro.petshelter.repositories.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<Report> getReportsByChatId(Long chatId) {
        return reportRepository.findReportsByChatId(chatId);
    }

    public List<Report> getReportsByDate(LocalDate date) {
        return reportRepository.findReportsByDate(date);
    }
}
