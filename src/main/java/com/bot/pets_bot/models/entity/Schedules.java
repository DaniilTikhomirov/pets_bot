package com.bot.pets_bot.models.entity;

import com.bot.pets_bot.models.dto.SchedulesDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность для хранения расписания работы.
 * Этот класс содержит расписание работы приюта на каждый день недели.
 */
@Entity
@Table(name = "schedules")
@Getter
@Setter
public class Schedules {

    /**
     * Уникальный идентификатор записи расписания.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Расписание на понедельник.
     * Содержит информацию о времени работы приюта в понедельник.
     */
    private String monday;

    /**
     * Расписание на вторник.
     * Содержит информацию о времени работы приюта во вторник.
     */
    private String tuesday;

    /**
     * Расписание на среду.
     * Содержит информацию о времени работы приюта в среду.
     */
    private String wednesday;

    /**
     * Расписание на четверг.
     * Содержит информацию о времени работы приюта в четверг.
     */
    private String thursday;

    /**
     * Расписание на пятницу.
     * Содержит информацию о времени работы приюта в пятницу.
     */
    private String friday;

    /**
     * Расписание на субботу.
     * Содержит информацию о времени работы приюта в субботу.
     */
    private String saturday;

    /**
     * Расписание на воскресенье.
     * Содержит информацию о времени работы приюта в воскресенье.
     */
    private String sunday;

    /**
     * Информация о приюте, с которым связано это расписание.
     * Связь один к одному с сущностью {@link ShelterInfo}.
     */
    @OneToOne(mappedBy = "schedules")
    @JsonIgnore
    private ShelterInfo shelterInfo;

    /**
     * Преобразует объект {@link SchedulesDTO} в сущность {@link Schedules}.
     *
     * @param dto объект {@link SchedulesDTO}, содержащий данные для преобразования.
     * @return объект {@link Schedules} с данными из DTO.
     */
    public static Schedules convertFromDTO(SchedulesDTO dto) {
        Schedules schedules = new Schedules();
        schedules.setMonday(dto.getMonday());
        schedules.setTuesday(dto.getTuesday());
        schedules.setWednesday(dto.getWednesday());
        schedules.setThursday(dto.getThursday());
        schedules.setFriday(dto.getFriday());
        schedules.setSaturday(dto.getSaturday());
        schedules.setSunday(dto.getSunday());
        return schedules;
    }
}
