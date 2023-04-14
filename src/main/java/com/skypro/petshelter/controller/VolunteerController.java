package com.skypro.petshelter.controller;

import com.skypro.petshelter.entity.Volunteer;
import com.skypro.petshelter.service.VolunteersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("volunteer")
public class VolunteerController {

    private final VolunteersService volunteersService;

    public VolunteerController(VolunteersService volunteersService) {
        this.volunteersService = volunteersService;
    }

    @Operation(summary = "Добавление нового волонтера в базу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Добавленный волонтер",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    )
            })
    @PostMapping
    public ResponseEntity<Volunteer> addVolunteer(@Parameter(description = "Идентификатор чата") @RequestParam Long chatId,
                                                  @Parameter(description = "Имя волонтера", example = "Иванов Иван") @RequestParam String name,
                                                  @Parameter(description = "Номер телефона", example = "89990000000") @RequestParam String contact) {
        return ResponseEntity.ok(volunteersService.addVolunteer(chatId, name, contact));
    }

    @Operation(summary = "Удаление волонтера из базы",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удаленый волонтер",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Volunteer.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Такого в базе нет"
                    )
            })
    @DeleteMapping
    public ResponseEntity<Volunteer> deleteVolunteer(
            @Parameter(description = "Идентификатор чата") @RequestParam Long chatId) {
        Volunteer volunteer = volunteersService.deleteVolunteer(chatId);
        if (volunteer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volunteer);
    }
}
