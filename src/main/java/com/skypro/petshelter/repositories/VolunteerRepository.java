package com.skypro.petshelter.repositories;

import com.skypro.petshelter.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository extends JpaRepository<Volunteer,Long> {
}
