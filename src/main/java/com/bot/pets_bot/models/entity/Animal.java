package com.bot.pets_bot.models.entity;

import com.bot.pets_bot.models.dto.AnimalDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Сущность, представляющая животное, доступное для усыновления.
 * Содержит информацию о животном, такую как имя, возраст, цвет, описание и статус.
 */
@Entity
@Table(name = "animals")
@Getter
@Setter
public class Animal {

    /**
     * Уникальный идентификатор животного.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Имя животного.
     */
    @Column(name = "name", columnDefinition = "TEXT")
    @Lob
    private String name;

    /**
     * Описание животного.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    @Lob
    private String description;

    /**
     * Цвет животного.
     */
    @Column(name = "color")
    private String color;

    /**
     * Возраст животного.
     */
    @Column(name = "age")
    private int age;

    /**
     * Вид или порода животного.
     */
    @Column(name = "kind", columnDefinition = "TEXT")
    @Lob
    private String kind;

    /**
     * URL фотографии животного.
     */
    @Column(name = "photo_url", columnDefinition = "TEXT")
    @Lob
    private String photoUrl;

    /**
     * Флаг, указывающий, было ли животное забрано (усыновлено).
     * По умолчанию значение `false`.
     */
    @Column(name = "take", columnDefinition = "boolean default(false)")
    private boolean take;

    @Column(name = "cat", columnDefinition = "boolean default(false)")
    private boolean cat;

    /**
     * Статус животного, связанный с сущностью {@link Status}.
     */
    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    /**
     * Усыновитель животного, связанный с сущностью {@link Adopter}.
     */
    @OneToOne
    @JsonIgnore
    private Adopter adopter;

    /**
     * Список отчетов, связанных с животным.
     * Связано с сущностью {@link Reports}.
     */
    @OneToMany(mappedBy = "animal")
    @JsonIgnore
    private List<Reports> reports;

    /**
     * Метод для преобразования объекта {@link AnimalDTO} в сущность {@link Animal}.
     *
     * @param dto объект {@link AnimalDTO}, содержащий данные для преобразования.
     * @return объект {@link Animal}, созданный на основе переданного DTO.
     */
    public static Animal convertFromDTO(AnimalDTO dto) {
        Animal animal = new Animal();
        animal.setName(dto.getName());
        animal.setDescription(dto.getDescription());
        animal.setColor(dto.getColor());
        animal.setAge(dto.getAge());
        animal.setKind(dto.getKind());
        animal.setCat(dto.isCat());
        return animal;
    }
}
