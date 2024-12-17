package com.bot.pets_bot.repositories;

import com.bot.pets_bot.models.entity.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramUsersRepository extends JpaRepository<TelegramUser, Long> {
}
