package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.TelegramUserDTO;
import com.bot.pets_bot.models.entity.TelegramUser;
import com.bot.pets_bot.repositories.TelegramUsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с пользователями Telegram.
 * Предоставляет методы для добавления, обновления и получения пользователей по их Telegram ID.
 */
@Service
public class TelegramUserService {

    private final TelegramUsersRepository telegramUsersRepository;

    /**
     * Конструктор сервиса для инициализации репозитория пользователей Telegram.
     *
     * @param telegramUsersRepository Репозиторий для работы с пользователями Telegram.
     */
    public TelegramUserService(TelegramUsersRepository telegramUsersRepository) {
        this.telegramUsersRepository = telegramUsersRepository;
    }

    /**
     * Получает список всех пользователей Telegram.
     *
     * @return Список объектов TelegramUser.
     */
    public List<TelegramUser> getTelegramUsers() {
        return telegramUsersRepository.findAll();
    }

    /**
     * Добавляет нового пользователя Telegram.
     *
     * @param dto Объект DTO, содержащий информацию о пользователе.
     * @return Сохраненный объект TelegramUser.
     */
    public TelegramUser addTelegramUser(TelegramUserDTO dto) {
        TelegramUser telegramUser = TelegramUser.convertFromDTO(dto);
        return telegramUsersRepository.save(telegramUser);
    }

    /**
     * Обновляет информацию о пользователе Telegram.
     *
     * @param telegramUser Обновленный объект TelegramUser.
     * @return Обновленный объект TelegramUser.
     */
    public TelegramUser putTelegramUser(TelegramUser telegramUser) {
        return telegramUsersRepository.save(telegramUser);
    }

    /**
     * Получает пользователя Telegram по его Telegram ID.
     *
     * @param telegramId ID пользователя в Telegram.
     * @return Объект TelegramUser или null, если пользователь не найден.
     */
    public TelegramUser getTelegramUserByTelegramId(long telegramId) {
        return telegramUsersRepository.findByTelegramId(telegramId).orElse(null);
    }

    /**
     * Проверяет, является ли пользователь новым, основываясь на его Telegram ID.
     *
     * @param telegramId ID пользователя в Telegram.
     * @return true, если пользователь новый, иначе false.
     */
    public boolean isNewUserByTelegramId(long telegramId) {
        return getTelegramUserByTelegramId(telegramId) == null;
    }
}
