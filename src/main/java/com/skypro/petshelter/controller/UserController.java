package com.skypro.petshelter.controller;

import com.skypro.petshelter.entity.User;
import com.skypro.petshelter.entity.Volunteer;
import com.skypro.petshelter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Добавление нового пользователя в базу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Добавленный пользователь",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    )
            })
    @PostMapping
    public ResponseEntity<User> addUser(@Parameter(description = "Идентификатор чата") @RequestParam Long chatId,
                                        @Parameter(description = "Имя пользователя", example = "Иванов Иван")
                                        @RequestParam String name) {
        return ResponseEntity.ok(userService.addUser(chatId, name));
    }

    @Operation(summary = "Удаление пользователя из базы",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Удаленый пользователь",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Такого в базе нет"
                    )
            })
    @DeleteMapping
    public ResponseEntity<User> deleteUser(@Parameter(description = "Идентификатор чата") @RequestParam Long chatId) {
        User user = userService.deleteUser(chatId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Присвоение пользователю собаки",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Новый хозяин",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Такого в базе нет"
                    )
            })
    @PutMapping("give-dog")
    public ResponseEntity<User> giveDog(@Parameter(description = "Идентификатор чата") @RequestParam Long chatId,
                                        @Parameter(description = "Кличка собаки", example = "Бобик")
                                        @RequestParam String dogName) {
        if (userService.findUser(chatId) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.moveDog(chatId, dogName));
    }

    @Operation(summary = "Лишение пользователя собаки",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Бывший хозяин",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Такого в базе нет"
                    )
            })
    @PutMapping("deprive-dog")
    public ResponseEntity<User> depriveDog(@Parameter(description = "Идентификатор чата") @RequestParam Long chatId,
                                           @Parameter(description = "Кличка собаки", example = "Бобик")
                                           @RequestParam String dogName) {
        if (userService.findUser(chatId) == null
                || !dogName.equals(userService.findUser(chatId).getDogName())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.moveDog(chatId, null));
    }

    @Operation(summary = "Изменение испытательного срока для пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Хозяин собаки",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Такого в базе нет"
                    )
            })
    @PutMapping("set-trial")
    public ResponseEntity<User> setDaysTrial(@Parameter(description = "Идентификатор чата") @RequestParam Long chatId,
                                             @Parameter(description = "Новый срок", example = "30") @RequestParam Integer daysTrial) {
        if (userService.findUser(chatId) == null
                || userService.findUser(chatId).getDogName() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.setTrialDays(chatId, daysTrial));
    }


}
