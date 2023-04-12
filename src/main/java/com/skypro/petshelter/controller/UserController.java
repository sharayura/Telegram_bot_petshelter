package com.skypro.petshelter.controller;

import com.skypro.petshelter.entity.User;
import com.skypro.petshelter.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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


}
