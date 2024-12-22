package com.bot.pets_bot.models.dto;

import com.bot.pets_bot.anotation.ValidContact;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) для информации о приюте для животных.
 * Этот класс используется для передачи данных о приюте, таких как имя, описание,
 * контактная информация безопасности, адрес, схема проезда, меры безопасности и расписание работы.
 */
@Getter
@Setter
public class ShelterInfoDTO {

    /**
     * Имя приюта.
     * Название приюта для отображения в интерфейсе.
     */
    private String name;

    /**
     * Описание приюта.
     * Краткое описание приюта, его миссии и услуги.
     */
    private String description;

    /**
     * Контактная информация для безопасности.
     * Номер телефона или другие контактные данные для связи в случае экстренных ситуаций.
     */
    @ValidContact
    private String securityContact;

    /**
     * Адрес приюта.
     * Адрес приюта, где его можно найти.
     */
    private String address;

    /**
     * Схема проезда к приюту.
     * URL-ссылка на фотографию или схему, которая помогает добраться до приюта.
     */
    private String schemaForRoadPhotoUrl;

    /**
     * Меры безопасности в приюте.
     * Описание правил и мер безопасности, которые следует соблюдать при посещении приюта.
     */
    private String safetyPrecautions;

    /**
     * Расписание работы приюта.
     * Содержит расписание работы приюта для каждого дня недели.
     */
    private SchedulesDTO schedules;
}
