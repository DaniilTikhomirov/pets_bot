package com.bot.pets_bot.models.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) для владельца животного.
 * Используется для передачи данных владельца, таких как идентификатор Telegram и имя.
 */
@Getter
@Setter
public class OwnerDTO {

    /**
     * Идентификатор Telegram владельца.
     * Уникальный идентификатор пользователя в Telegram.
     */
    private long telegramId;

    /**
     * Имя владельца.
     * Строка, содержащая имя владельца животного.
     */
    private String name;
}
