package com.bot.pets_bot.repositories;

import com.bot.pets_bot.models.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
}
