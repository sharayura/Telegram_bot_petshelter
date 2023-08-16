package com.skypro.petshelter.repositories;

import com.skypro.petshelter.entity.UserContext;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserContextRepository extends JpaRepository<UserContext, Long> {
}
