package com.skypro.petshelter.controller;

import com.skypro.petshelter.entity.Report;
import com.skypro.petshelter.service.ReportService;
import com.skypro.petshelter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("report")
public class ReportController {

    private final ReportService reportService;
    private final UserService userService;

    public ReportController(ReportService reportService, UserService userService) {
        this.reportService = reportService;
        this.userService = userService;
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
    @GetMapping("date")
    public ResponseEntity<List<Report>> getReportsByDate(@Parameter(description = "Дата", example = "10-05-2023")
                                                           @RequestParam LocalDate date) {
        return ResponseEntity.ok(reportService.getReportsByDate(date));
    }
}
