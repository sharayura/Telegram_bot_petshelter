package com.skypro.petshelter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PetshelterApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetshelterApplication.class, args);
    }

}
