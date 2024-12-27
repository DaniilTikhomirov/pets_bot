package com.bot.pets_bot.models.dto;

import com.bot.pets_bot.anotation.ValidContact;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) для передачи данных о кинологе.
 * Используется для обмена данными между слоями приложения.
 */
@Getter
@Setter
public class DogHandlerDTO {

    /**
     * Имя кинолога.
     */
    private String name;

    /**
     * Фамилия кинолога.
     */
    private String secondName;

    /**
     * Контактные данные кинолога.
     * Проверяются на корректность с использованием аннотации {@link ValidContact}.
     */
    @ValidContact
    private String contact;

    /**
     * Описание или информация о кинологе.
     */
    private String description;
}
