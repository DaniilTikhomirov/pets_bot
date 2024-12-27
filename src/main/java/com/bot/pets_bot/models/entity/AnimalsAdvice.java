package com.bot.pets_bot.models.entity;

import com.bot.pets_bot.models.dto.AnimalsAdviceDTO;
import com.bot.pets_bot.models.dto.DogHandlerDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий общую информацию, связанную с содержанием и уходом за животными.
 * Содержит правила, рекомендации, документы и причины отказа, которые связаны с животными.
 */
@Entity
@Table(name = "animals_advice")
@Getter
@Setter
public class AnimalsAdvice {

    /**
     * Уникальный идентификатор записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Правила получения животного.
     */
    @Lob
    @Column(name = "rules_for_getting_animal", nullable = false, columnDefinition = "TEXT")
    private String rulesForGettingAnimal;

    /**
     * Документы, необходимые для получения животного.
     */
    @Lob
    @Column(name = "documents_for_animal", nullable = false, columnDefinition = "TEXT")
    private String documentsForAnimal;

    /**
     * Рекомендации по транспортировке животного.
     */
    @Lob
    @Column(name = "recommendation_for_move_animal", nullable = false, columnDefinition = "TEXT")
    private String recommendationForMoveAnimal;

    /**
     * Рекомендации по обустройству для щенка.
     */
    @Lob
    @Column(name = "recommendation_for_arrangement_for_puppy", nullable = false, columnDefinition = "TEXT")
    private String recommendationForArrangementForPuppy;

    /**
     * Рекомендации по обустройству для собаки.
     */
    @Lob
    @Column(name = "recommendation_for_arrangement_for_dog", nullable = false, columnDefinition = "TEXT")
    private String recommendationForArrangementForDog;

    /**
     * Рекомендации по обустройству для животных с ограниченными возможностями.
     */
    @Lob
    @Column(name = "recommendation_for_arrangement_for_disability", nullable = false, columnDefinition = "TEXT")
    private String recommendationForArrangementForDisability;

    /**
     * Советы кинолога по уходу и дрессировке собак.
     */
    @Lob
    @Column(name = "dog_handler_advice", nullable = false, columnDefinition = "TEXT")
    private String dogHandlerAdvice;

    /**
     * Причины отказа в получении животного.
     */
    @Lob
    @Column(name = "reasons_for_refusal", nullable = false, columnDefinition = "TEXT")
    private String reasonsForRefusal;

    /**
     * Список кинологов, связанных с советами по уходу за животными.
     */
    @OneToMany(mappedBy = "advice", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<DogHandler> dogHandlers;

    /**
     * Конвертация объекта DTO {@link AnimalsAdviceDTO} в сущность {@link AnimalsAdvice}.
     *
     * @param dto объект DTO с данными.
     * @return экземпляр {@link AnimalsAdvice}.
     */
    public static AnimalsAdvice convertFromDTO(AnimalsAdviceDTO dto) {
        AnimalsAdvice advice = new AnimalsAdvice();
        advice.setRulesForGettingAnimal(dto.getRulesForGettingAnimal());
        advice.setDocumentsForAnimal(dto.getDocumentsForAnimal());
        advice.setRecommendationForMoveAnimal(dto.getRecommendationForMoveAnimal());
        advice.setRecommendationForArrangementForPuppy(dto.getRecommendationForArrangementForPuppy());
        advice.setRecommendationForArrangementForDog(dto.getRecommendationForArrangementForDog());
        advice.setRecommendationForArrangementForDisability(dto.getRecommendationForArrangementForDisability());
        advice.setDogHandlerAdvice(dto.getDogHandlerAdvice());
        advice.setReasonsForRefusal(dto.getReasonsForRefusal());
        List<DogHandlerDTO> dogHandlersDTO = dto.getDogHandlers();
        if (dogHandlersDTO != null && !dogHandlersDTO.isEmpty()) {
            List<DogHandler> dogHandlers = dogHandlersDTO.stream().map(o -> {
                DogHandler dogHandler = DogHandler.convertFromDTO(o);
                dogHandler.setAdvice(advice);
                return dogHandler;
            }).toList();
            advice.setDogHandlers(dogHandlers);;
        }

        return advice;
    }
}
