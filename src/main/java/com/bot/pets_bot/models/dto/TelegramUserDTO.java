package com.bot.pets_bot.models.dto;

import com.bot.pets_bot.anotation.ValidContact;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс, представляющий Data Transfer Object (DTO) для пользователя Telegram.
 * Этот объект используется для передачи данных о пользователе Telegram между слоями приложения,
 * например, между контроллером и сервисом или между сервисом и репозиторием.
 */
@Getter
@Setter
public class TelegramUserDTO {

    /**
     * Уникальный идентификатор пользователя в Telegram.
     * Этот идентификатор используется для идентификации пользователя в системе.
     */
    public long telegramId;

    /**
     * Контактная информация пользователя.
     * Поле с аннотацией @ValidContact, которое гарантирует, что контактные данные будут валидными.
     */
    @ValidContact
    private String contact;

    /**
     * Имя пользователя.
     * Имя, которое указано в профиле пользователя Telegram.
     */
    private String name;
}
