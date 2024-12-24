package com.bot.pets_bot.models.entity;

import com.bot.pets_bot.models.dto.AnimalDTO;
import com.bot.pets_bot.models.dto.StatusDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Класс, представляющий статус животного.
 * Статус может быть использован для отслеживания состояния животного (например, "доступен для усыновления",
 * "находится в приюте", "усыновлен" и т.д.).
 * Этот класс связывает статус с животными, которым он был назначен.
 */
@Entity
@Table(name = "status")
@Getter
@Setter
public class Status {

    /**
     * Уникальный идентификатор статуса.
     * Это поле используется для сохранения и извлечения статуса из базы данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Название статуса.
     * Название статуса, например, "Доступен для усыновления" или "Усыновлен".
     */
    @Column(name = "status_name")
    private String statusName;

    /**
     * Список животных, которые имеют этот статус.
     * Связь с сущностью Animal, которая хранит информацию о животных, находящихся в этом статусе.
     */
    @OneToMany(mappedBy = "status", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Animal> animals;

    /**
     * Метод для преобразования объекта DTO в сущность Status.
     * @param dto объект Data Transfer Object, содержащий информацию о статусе.
     * @return новый объект Status, заполненный данными из DTO.
     */
    public static Status convertFromDTO(StatusDTO dto) {
        Status status = new Status();
        status.setStatusName(dto.getStatusName());
        return status;
    }
}
