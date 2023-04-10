package com.skypro.petshelter.repositories;

import com.skypro.petshelter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
