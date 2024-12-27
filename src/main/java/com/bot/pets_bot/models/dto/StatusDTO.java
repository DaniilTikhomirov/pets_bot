package com.bot.pets_bot.models.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс, представляющий Data Transfer Object (DTO) для статуса животного.
 * Этот объект используется для передачи данных о статусе животного между слоями приложения,
 * например, между контроллером и сервисом или между сервисом и репозиторием.
 */
@Getter
@Setter
public class StatusDTO {

    /**
     * Название статуса.
     * Название статуса животного, например, "Доступен для усыновления" или "Усыновлен".
     */
    private String statusName;
}
