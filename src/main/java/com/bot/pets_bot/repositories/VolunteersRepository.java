package com.bot.pets_bot.repositories;

import com.bot.pets_bot.models.entity.Volunteers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteersRepository extends JpaRepository<Volunteers, Long> {
}
