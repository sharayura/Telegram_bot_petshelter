package com.skypro.petshelter.repositories;

import com.skypro.petshelter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findUsersByDaysTrialNotNull();
    List<User> findUsersByDogNameNotNull();
}
