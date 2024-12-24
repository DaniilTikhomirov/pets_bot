package com.bot.pets_bot.models.dto;

import com.bot.pets_bot.anotation.ValidContact;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO-класс (Data Transfer Object) для передачи данных усыновителя (adopter)
 * между уровнями приложения.
 * Содержит минимальный набор полей для взаимодействия с клиентами.
 */
@Schema(description = "DTO для передачи данных усыновителя")
@Getter
@Setter
public class AdopterDTO {

    /**
     * Имя усыновителя.
     */
    @Schema(description = "Имя усыновителя", example = "Иван Иванов")
    private String name;

    /**
     * Контактная информация усыновителя (например, телефон или email).
     * Проверяется на валидность аннотацией {@link ValidContact}.
     */
    @ValidContact
    @Schema(description = "Контактная информация усыновителя (телефон) в формате +x-xxx-xxx-xx-xx",
            example = "+7-777-777-77-77")
    private String contact;

    /**
     * Telegram ID, связанный с усыновителем.
     * Используется для идентификации в Telegram.
     */
    private long telegramId;
}
