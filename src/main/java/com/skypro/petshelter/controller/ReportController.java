package com.skypro.petshelter.controller;

import com.skypro.petshelter.entity.Report;
import com.skypro.petshelter.repositories.ReportRepository;
import com.skypro.petshelter.service.ReportService;
import com.skypro.petshelter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("report")
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;
    private final ReportRepository reportRepository;

    public ReportController(ReportService reportService, UserService userService,
                            ReportRepository reportRepository) {
        this.reportService = reportService;
        this.userService = userService;
        this.reportRepository = reportRepository;
    }

    @Operation(summary = "Получить все отчеты заданного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список отчетов",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Report.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Такого в базе нет"
                    )
            })
    @Transactional
    @GetMapping("chat-id")
    public ResponseEntity<List<Report>> getReportsByChatId(@Parameter(description = "Идентификатор чата")
                                                           @RequestParam Long chatId) {
        if (userService.findUser(chatId) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reportService.getReportsByChatId(chatId));
    }

    @Operation(summary = "Получить все отчеты на заданную дату",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список отчетов",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Report.class))
                            )
                    )
            })
    @Transactional
    @GetMapping("date")
    public ResponseEntity<List<Report>> getReportsByDate(@Parameter(description = "Дата", example = "2023-05-10")
                                                         @RequestParam String date) {
        return ResponseEntity.ok(reportService.getReportsByDate(LocalDate.parse(date)));
    }

    @Operation(summary = "Получить фото из конкретного отчета",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Фото",
                            content = @Content(
                                    mediaType = MediaType.IMAGE_JPEG_VALUE
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Такого в базе нет"
                    )
            })
    @GetMapping("photo")
    public ResponseEntity<byte[]> downloadPhoto(@Parameter(description = "Идентификатор чата")
                                                @RequestParam Long chatId,
                                                @Parameter(description = "Дата", example = "2023-05-10")
                                                @RequestParam String date) {
        Report report = reportRepository.findById(reportService.parseDateChatId(chatId, LocalDate.parse(date))).orElse(null);
        if (report == null || report.getPhoto() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("image/jpeg"));
        headers.setContentLength(report.getPhoto().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(report.getPhoto());
    }
}
