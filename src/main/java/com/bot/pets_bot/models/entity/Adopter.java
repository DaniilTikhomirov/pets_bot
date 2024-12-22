package com.bot.pets_bot.models.entity;

import com.bot.pets_bot.models.dto.AdopterDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Класс-сущность, представляющий усыновителя (adopter) в системе.
 * Связан с таблицей "adopter" в базе данных.
 * Содержит поля для персональной информации, статистики и связей с другими сущностями.
 */
@Entity
@Table(name = "adopter")
@Getter
@Setter
@Schema(description = "Сущность усыновителя")
public class Adopter {

    /**
     * Уникальный идентификатор усыновителя.
     * Генерируется автоматически базой данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор усыновителя", example = "1")
    private long id;

    /**
     * Имя усыновителя.
     * Поле не может быть null.
     */
    @Column(name = "name", nullable = false)
    @Schema(description = "Имя усыновителя", example = "Иван", required = true)
    private String name;

    /**
     * Контактная информация усыновителя (например, телефон или email).
     * Поле не может быть null.
     */
    @Column(name = "contact", nullable = false)
    @Schema(description = "Контактная информация усыновителя", example = "+7-777-777-77-77", required = true)
    private String contact;

    /**
     * Счётчик успешных действий усыновителя (например, отправленных отчётов).
     * По умолчанию равен 0.
     */
    @Column(name = "streak_counter", columnDefinition = "int default(0)")
    @Schema(description = "Счётчик успешных действий усыновителя", example = "0")
    private int streakCounter;

    /**
     * Флаг, указывающий, отправил ли отчёт усыновитель.
     * По умолчанию false.
     */
    @Column(name = "send_report", columnDefinition = "boolean default(false)")
    @Schema(description = "Флаг, указывающий, отправил ли отчёт усыновитель", example = "false")
    private boolean sendReport;

    /**
     * Счётчик предупреждений, полученных усыновителем.
     * По умолчанию равен 0.
     */
    @Column(name = "warning_counter", columnDefinition = "int default(0)")
    @Schema(description = "Счётчик предупреждений, полученных усыновителем", example = "0")
    private int warningCounter;

    /**
     * Telegram ID, связанный с усыновителем.
     * Используется для связи через Telegram.
     * Поле не может быть null.
     */
    @Column(name = "telegram_id", nullable = false)
    @Schema(description = "Telegram ID усыновителя", example = "123456789", required = true)
    private long telegramId;

    /**
     * Счётчик последовательных предупреждений, полученных усыновителем.
     * По умолчанию равен 0.
     */
    @Column(name = "warning_streak_counter", columnDefinition = "int default(0)")
    @Schema(description = "Счётчик последовательных предупреждений усыновителя", example = "0")
    private int warningStreakCounter;

    /**
     * Животное, связанное с усыновителем.
     * Одно к одному.
     */
    @OneToOne(mappedBy = "adopter")
    @Schema(description = "Животное, связанное с усыновителем")
    private Animal animal;

    /**
     * Список отчётов, связанных с усыновителем.
     * Поле игнорируется при сериализации в JSON.
     */
    @OneToMany(mappedBy = "adopter")
    @Schema(description = "Список отчётов, связанных с усыновителем", hidden = true)
    @JsonIgnore
    private List<Reports> reports;

    /**
     * Метод для преобразования DTO-объекта {@link AdopterDTO} в сущность {@link Adopter}.
     *
     * @param dto объект {@link AdopterDTO}, содержащий данные для преобразования.
     * @return объект {@link Adopter}, созданный на основе DTO.
     */
    public static Adopter convertFromDTO(AdopterDTO dto) {
        Adopter adopter = new Adopter();
        adopter.setName(dto.getName());
        adopter.setContact(dto.getContact());
        adopter.setTelegramId(dto.getTelegramId());
        return adopter;
    }
}
