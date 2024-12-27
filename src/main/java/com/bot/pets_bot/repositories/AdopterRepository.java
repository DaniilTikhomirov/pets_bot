package com.bot.pets_bot.repositories;

import com.bot.pets_bot.models.entity.Adopter;
import com.bot.pets_bot.models.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdopterRepository extends JpaRepository<Adopter, Long> {
    Optional<Adopter> findFirstByTelegramId(long telegramId);
}
