package com.bot.pets_bot.models.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO (Data Transfer Object) для представления данных о животном.
 * Используется для передачи информации между слоями приложения.
 */
@Getter
@Setter
@Data
@ToString
public class AnimalDTO {

    /**
     * Имя животного.
     */
    private String name;

    /**
     * Описание животного.
     */
    private String description;

    /**
     * Цвет животного.
     */
    private String color;

    /**
     * Возраст животного.
     */
    private int age;

    /**
     * Вид или порода животного.
     */
    private String kind;
}
