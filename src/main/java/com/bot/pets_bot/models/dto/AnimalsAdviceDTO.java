package com.bot.pets_bot.models.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO-класс, представляющий информацию о правилах и рекомендациях, связанных с содержанием животных.
 * Используется для передачи данных между слоями приложения.
 */
@Getter
@Setter
public class AnimalsAdviceDTO {

    /**
     * Правила получения животного.
     */
    private String rulesForGettingAnimal;

    /**
     * Список документов, необходимых для получения животного.
     */
    private String documentsForAnimal;

    /**
     * Рекомендации по транспортировке животного.
     */
    private String recommendationForMoveAnimal;

    /**
     * Рекомендации по обустройству для щенка.
     */
    private String recommendationForArrangementForPuppy;

    /**
     * Рекомендации по обустройству для собаки.
     */
    private String recommendationForArrangementForDog;

    /**
     * Рекомендации по обустройству для животных с ограниченными возможностями.
     */
    private String recommendationForArrangementForDisability;

    /**
     * Советы кинолога по уходу за животными.
     */
    private String dogHandlerAdvice;

    /**
     * Причины отказа в получении животного.
     */
    private String reasonsForRefusal;

    /**
     * Список данных о кинологах, связанных с рекомендациями по содержанию животных.
     * Каждый элемент списка представляет собой объект {@link DogHandlerDTO}.
     */
    @Valid
    private List<DogHandlerDTO> dogHandlers;
}
