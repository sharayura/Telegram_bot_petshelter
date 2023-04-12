package com.skypro.petshelter.controller;

import com.skypro.petshelter.entity.Volunteer;
import com.skypro.petshelter.service.VolunteersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VolunteerController {

    private final VolunteersService volunteersService;

    public VolunteerController(VolunteersService volunteersService) {
        this.volunteersService = volunteersService;
    }

    @PostMapping("add")
    public ResponseEntity<Volunteer> addVolunteer(@RequestParam Long chatId,
                                                  @RequestParam String name,
                                                  @RequestParam String contact) {
        return ResponseEntity.ok(volunteersService.addVolunteer(chatId, name, contact));
    }

    @DeleteMapping("delete")
    public ResponseEntity<Volunteer> deleteVolunteer(@RequestParam Long chatId) {
        Volunteer volunteer = volunteersService.deleteVolunteer(chatId);
        if (volunteer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(volunteer);
    }
}
