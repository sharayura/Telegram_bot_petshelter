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
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

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
    public void savePhoto(Long chatId, File file) {
       Report report = createReport(chatId);
        byte[] data = null;
        try {
            data = Files.readAllBytes(file.getAbsoluteFile().toPath());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        report.setPhoto(data);

    }


//    public void imageResizer() {
//        try {
//            BufferedImage originalImage = ImageIO.read(new File("original.jpg"));
//            int newWidth = 800; // задайте новую ширину
//            int newHeight = (int) Math.round(originalImage.getHeight() * newWidth / (double) originalImage.getWidth());
//            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
//            Graphics2D g = resizedImage.createGraphics();
//            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
//            g.dispose();
//            ImageIO.write(resizedImage, "jpg", new File("resized.jpg"));
//        } catch (Exception e) {
//            logger.error("Ошибка обработки фото: " + e.getMessage(), e);
//        }
//    }

}
