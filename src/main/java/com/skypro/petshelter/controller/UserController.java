package com.skypro.petshelter.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.petshelter.entity.User;
import com.skypro.petshelter.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final TelegramBot telegramBot;

    public UserController(UserService userService, TelegramBot telegramBot) {
        this.userService = userService;
        this.telegramBot = telegramBot;
    }

    @PostMapping("add")
    public ResponseEntity<User> addUser(@RequestParam Long chatId,
                                        @RequestParam String name) {
        return ResponseEntity.ok(userService.addUser(chatId, name));
    }

    @DeleteMapping("delete")
    public ResponseEntity<User> deleteUser(@RequestParam Long chatId) {
        User user = userService.deleteUser(chatId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PutMapping("give-dog")
    public ResponseEntity<User> giveDog(@RequestParam Long chatId,
                                        @RequestParam String dogName) {
        if (userService.findUser(chatId) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userService.moveDog(chatId, dogName));
    }

    @PutMapping("deprive-dog")
    public ResponseEntity<User> depriveDog(@RequestParam Long chatId,
                                           @RequestParam String dogName) {
        if (userService.findUser(chatId) == null
                || !dogName.equals(userService.findUser(chatId).getDogName())) {
            return ResponseEntity.notFound().build();
        }
        SendMessage message = new SendMessage(chatId,
                "К сожалению, Вы не прошли испытательный срок, верните бедное животное!!!");
        telegramBot.execute(message);
        return ResponseEntity.ok(userService.moveDog(chatId, null));
    }

    @PutMapping("set-trial")
    public ResponseEntity<User> setTrialDays(@RequestParam Long chatId,
                                             @RequestParam Integer trialDays) {
        if (userService.findUser(chatId) == null
                || userService.findUser(chatId).getDogName() == null) {
            return ResponseEntity.notFound().build();
        }
        SendMessage message = new SendMessage(chatId,
                "Ваш испытательный срок изменен, осталось еще " + trialDays + " дней.");
        telegramBot.execute(message);
        return ResponseEntity.ok(userService.setTrialDays(chatId, trialDays));
    }


}
