package com.skypro.petshelter.repositories;

import com.skypro.petshelter.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report,String> {
   List<Report> findReportsByChatId(Long chatId);
   List<Report> findReportsByDate(LocalDate date);
}
