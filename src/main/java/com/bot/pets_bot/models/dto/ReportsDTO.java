package com.bot.pets_bot.models.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс Data Transfer Object (DTO) для отчетов.
 * Этот класс используется для передачи данных отчета, включая текст отчета,
 * идентификаторы усыновителя, животного и волонтера, связанных с отчетом.
 */
@Getter
@Setter
public class ReportsDTO {

    /**
     * Текст отчета.
     * Содержит описание или информацию, предоставленную в отчете.
     */
    private String text;

    /**
     * Идентификатор усыновителя, связанного с отчетом.
     * Используется для связи отчета с конкретным усыновителем.
     */
    private long adopterId;

    /**
     * Идентификатор животного, связанного с отчетом.
     * Используется для связи отчета с конкретным животным.
     */
    private long animalId;

    /**
     * Идентификатор волонтера, связанного с отчетом.
     * Используется для связи отчета с конкретным волонтером.
     */
    private long volunteerId;
}
