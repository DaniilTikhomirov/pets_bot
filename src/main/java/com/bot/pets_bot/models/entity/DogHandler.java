package com.bot.pets_bot.models.entity;

import com.bot.pets_bot.models.dto.DogHandlerDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность, представляющая информацию о кинологе.
 * Используется для хранения данных о кинологах в базе данных.
 */
@Entity
@Table(name = "dog_handler")
@Getter
@Setter
public class DogHandler {

    /**
     * Уникальный идентификатор кинолога.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Имя кинолога.
     */
    @Lob
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    /**
     * Фамилия кинолога.
     */
    @Lob
    @Column(name = "second_name", nullable = false, columnDefinition = "TEXT")
    private String secondName;

    /**
     * Контактные данные кинолога.
     */
    @Column(name = "contact", nullable = false)
    private String contact;

    /**
     * Описание или информация о кинологе.
     */
    @Lob
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * URL фотографии кинолога.
     */
    @Lob
    @Column(name = "photo_url", columnDefinition = "TEXT")
    private String photoUrl;

    /**
     * Ссылка на объект {@link AnimalsAdvice}, представляющий рекомендации, с которыми связан кинолог.
     */
    @ManyToOne
    @JoinColumn(name = "animals_advise_id")
    @JsonIgnore
    private AnimalsAdvice advice;

    /**
     * Метод для преобразования объекта {@link DogHandlerDTO} в сущность {@link DogHandler}.
     *
     * @param dto объект {@link DogHandlerDTO}, содержащий данные для преобразования.
     * @return объект {@link DogHandler}, созданный на основе переданных данных.
     */
    public static DogHandler convertFromDTO(DogHandlerDTO dto) {
        DogHandler handler = new DogHandler();
        handler.setName(dto.getName());
        handler.setSecondName(dto.getSecondName());
        handler.setContact(dto.getContact());
        handler.setDescription(dto.getDescription());
        return handler;
    }
}
