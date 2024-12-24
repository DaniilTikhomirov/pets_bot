package com.bot.pets_bot.models.dto;

import com.bot.pets_bot.anotation.ValidContact;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) для волонтера.
 * Этот класс используется для передачи данных о волонтере между слоями приложения,
 * например, при получении данных с клиента или отправке данных на сервер.
 */
@Getter
@Setter
public class VolunteersDTO {

    /**
     * Уникальный идентификатор пользователя в Telegram.
     * Этот идентификатор используется для связи волонтера с его аккаунтом в Telegram.
     */
    private long telegramId;

    /**
     * Контактная информация волонтера.
     * Включает данные для связи с волонтером.
     * Валидируется с использованием аннотации {@link ValidContact}.
     */
    @ValidContact
    private String contact;

    /**
     * Имя волонтера.
     * Хранит имя волонтера в виде строки.
     */
    private String name;

    /**
     * Фамилия волонтера.
     * Хранит фамилию волонтера в виде строки.
     */
    private String secondName;
}
