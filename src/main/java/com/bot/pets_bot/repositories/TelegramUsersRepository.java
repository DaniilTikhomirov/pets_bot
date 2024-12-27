package com.bot.pets_bot.repositories;

import com.bot.pets_bot.models.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelegramUsersRepository extends JpaRepository<TelegramUser, Long> {
    Optional<TelegramUser> findByTelegramId(long telegramId);
}
