package com.bot.pets_bot.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс сущности для отчетов, связанных с животными и их усыновителями.
 * Этот класс представляет отчет, содержащий информацию о фото, тексте отчета,
 * а также статус принятия отчета и его связь с усыновителем, животным и волонтером.
 */
@Entity
@Table(name = "reports")
@Getter
@Setter
public class Reports {

    /**
     * Уникальный идентификатор отчета.
     * Генерируется автоматически при добавлении нового отчета в базу данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * URL фотографии, связанной с отчетом.
     * Может быть использован для отображения изображения, связанного с отчетом.
     */
    @Lob
    @Column(name = "photo_url", columnDefinition = "TEXT")
    private String photoUrl;

    /**
     * Текст отчета.
     * Описание или информация, предоставленная в отчете.
     */
    @Lob
    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;

    /**
     * Статус принятия отчета.
     * Значение по умолчанию — false (не принято). Если отчет принят, значение будет изменено на true.
     */
    @Column(name = "accepted", columnDefinition = "boolean default(false)")
    private boolean accepted;

    /**
     * Усыновитель, связанный с отчетом.
     * Это отношение многие к одному: один усыновитель может иметь несколько отчетов.
     */
    @ManyToOne
    @JoinColumn(name = "adopter_id")
    private Adopter adopter;

    /**
     * Животное, связанное с отчетом.
     * Это отношение многие к одному: одно животное может иметь несколько отчетов.
     */
    @ManyToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    /**
     * Волонтер, связанный с отчетом.
     * Это отношение многие к одному: один волонтер может быть связан с несколькими отчетами.
     */
    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    private Volunteers volunteers;
}
