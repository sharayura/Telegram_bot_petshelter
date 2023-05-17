package com.skypro.petshelter.service;

import com.skypro.petshelter.entity.Report;
import com.skypro.petshelter.repositories.ReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;



import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportService reportService;

    @Test
    void getReportsByChatIdTest() {
        Long chatId = 123L;
        List<Report> expectedReports = Arrays.asList(new Report(), new Report());
        when(reportRepository.findReportsByChatId(chatId)).thenReturn(expectedReports);

        List<Report> actualReports = reportService.getReportsByChatId(chatId);

        assertEquals(expectedReports, actualReports);
        Mockito.verify(reportRepository).findReportsByChatId(chatId);
    }
    @Test
    void getReportsByDateTest() {
        LocalDate date = LocalDate.now();
        List<Report> expectedReports = Arrays.asList(new Report(), new Report());
        when(reportRepository.findReportsByDate(date)).thenReturn(expectedReports);

        List<Report> actualReports = reportService.getReportsByDate(date);

        assertEquals(expectedReports, actualReports);
        Mockito.verify(reportRepository).findReportsByDate(date);
    }

    @Test
    void parseDateChatIdTest() {
        Long chatId = 123L;
        LocalDate date = LocalDate.now();
        String expectedDateChatId = date.toString() + '-' + chatId;

        String actualDateChatId = reportService.parseDateChatId(chatId, date);

        assertEquals(expectedDateChatId, actualDateChatId);
    }

    @Test
    void isReportPresentTrueTest() {
        Long chatId = 123L;
        LocalDate date = LocalDate.now();
        String reportId = reportService.parseDateChatId(chatId, date);
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(new Report()));

        Boolean isReportPresent = reportService.isReportPresent(chatId, date);

        assertTrue(isReportPresent);
        Mockito.verify(reportRepository).findById(reportId);
    }

    @Test
    void isReportPresentFalseTest() {
        Long chatId = 123L;
        LocalDate date = LocalDate.now();
        String reportId = reportService.parseDateChatId(chatId, date);
        when(reportRepository.findById(reportId)).thenReturn(Optional.empty());

        Boolean isReportPresent = reportService.isReportPresent(chatId, date);

        assertFalse(isReportPresent);
        Mockito.verify(reportRepository).findById(reportId);
    }

    @Test
    void getReportFoundTest() {
        Long chatId = 123L;
        LocalDate date = LocalDate.now();
        String reportId = reportService.parseDateChatId(chatId, date);
        Report expectedReport = new Report();
        when(reportRepository.findById(reportId)).thenReturn(Optional.of(expectedReport));

        Report actualReport = reportService.getReport(chatId, date);

        assertEquals(expectedReport, actualReport);
        Mockito.verify(reportRepository).findById(reportId);
    }

    @Test
    void getReportNotFoundTest() {
        Long chatId = 123L;
        LocalDate date = LocalDate.now();
        String reportId = reportService.parseDateChatId(chatId, date);
        when(reportRepository.findById(reportId)).thenReturn(Optional.empty());

        Report actualReport = reportService.getReport(chatId, date);

        assertNull(actualReport);
        Mockito.verify(reportRepository).findById(reportId);
    }

    @Test
    void hasReportAnyNullAllFieldsNotNullTest() {
        Report report = new Report();
        report.setHabits("good");
        report.setHealth("good");
        report.setPhoto(new byte[]{});
        report.setRation("good");

        boolean hasNull = reportService.hasReportAnyNull(report);

        assertFalse(hasNull);
    }

    @Test
    void hasReportAnyNullOneFieldNullTest() {
        Report report = new Report();
        report.setHabits(null);
        report.setHealth("good");
        report.setPhoto(new byte[]{});
        report.setRation("good");

        boolean hasNull = reportService.hasReportAnyNull(report);

        assertTrue(hasNull);
    }

    @Test
    void replyToReportAllFieldsPresentTest() {
        Report report = new Report();
        report.setHabits("good");
        report.setHealth("good");
        report.setPhoto(new byte[]{});
        report.setRation("good");

        String expectedMessage = "Спасибо!";
        String actualMessage = reportService.replyToReport(report);

        assertEquals(expectedMessage, actualMessage);
    }

/*    @Test
    void savePhotoTest() throws IOException {
        Long chatId = 123L;
        File file = new File("test.jpg");

        byte[] imageData = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        Report report = new Report();
        report.setDate(LocalDate.now());
        report.setChatId(chatId);
        report.setDateChatId(LocalDate.now().toString() + "-" + chatId);

        Mockito.when(reportRepository.findById(report.getDateChatId()))
                .thenReturn(Optional.of(report));

        Mockito.when(reportRepository.save(report))
                .thenReturn(report);

        Report result = reportService.savePhoto(chatId, file);

        assertNotNull(result.getPhoto());
        assertArrayEquals(imageData, result.getPhoto());
        assertEquals(report.getDate(), result.getDate());
        assertEquals(chatId, result.getChatId());
        assertEquals(report.getDateChatId(), result.getDateChatId());
        Mockito.verify(reportRepository).findById(report.getDateChatId());
        Mockito.verify(reportRepository).save(report);
    }*/

    /*@Test
    public void testSaveRation() {
        // Создание mock ReportRepository
        reportRepository = mock(ReportRepository.class);

        // Создание ReportService с mock ReportRepository
        reportService = new ReportService(reportRepository);

        // Создание test data
        Long chatId = 123L;
        String ration = "test ration";

        // Создание mock Report
        Report mockReport = mock(Report.class);

        // Настройка mockReport для findById и save методов
        when(reportRepository.findById(anyString())).thenReturn(Optional.of(mockReport));
        when(reportRepository.save(any(Report.class))).thenReturn(mockReport);

        // Вызов метода saveRation
        reportService.saveRation(chatId, ration);


        // Проверка вызова save метода с mockReport, у которого установлено значение поля ration, равное test ration
        verify(reportRepository).save(argThat(report -> report.getRation().equals(ration)));
    }*/

    @Test
    void saveRationTest() {
        Long chatId = 123L;
        String rationText = "Миска с кормом и вода";
        ReportService reportService = new ReportService(reportRepository);

        Report savedReport = reportService.saveRation(chatId, rationText);

        assertNotNull(savedReport);
        assertEquals(rationText, savedReport.getRation());
    }

    @Test
    public void saveHabitsTest() {
        Long chatId = 123L;
        String habitsText = "Любит спать на диване";
        ReportService reportService = new ReportService(Mockito.mock(ReportRepository.class));

        Report savedReport = reportService.saveHabits(chatId, habitsText);

        assertNotNull(savedReport);
        assertEquals(habitsText, savedReport.getHabits());
    }

    @Test
    public void saveHealthTest() {
        Long chatId = 123L;
        String healthText = "Здоров, полон сил";
        ReportService reportService = new ReportService(Mockito.mock(ReportRepository.class));

        Report savedReport = reportService.saveHealth(chatId, healthText);

        assertNotNull(savedReport);
        assertEquals(healthText, savedReport.getHealth());
    }


}

