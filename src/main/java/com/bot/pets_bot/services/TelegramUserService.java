package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.TelegramUserDTO;
import com.bot.pets_bot.models.entity.TelegramUser;
import com.bot.pets_bot.repositories.TelegramUsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramUserService {

    private final TelegramUsersRepository telegramUsersRepository;

    public TelegramUserService(TelegramUsersRepository telegramUsersRepository) {
        this.telegramUsersRepository = telegramUsersRepository;
    }

    public List<TelegramUser> getTelegramUsers() {
        return telegramUsersRepository.findAll();
    }

    public TelegramUser addTelegramUser(TelegramUserDTO dto) {
        TelegramUser telegramUser = TelegramUser.convertFromDTO(dto);
        return telegramUsersRepository.save(telegramUser);
    }
}
