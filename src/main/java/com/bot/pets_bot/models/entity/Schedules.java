package com.bot.pets_bot.models.entity;

import com.bot.pets_bot.models.dto.SchedulesDTO;
import com.bot.pets_bot.models.dto.TelegramUserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "schedules")
@Getter
@Setter
public class Schedules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String monday;

    private String tuesday;

    private String wednesday;

    private String thursday;

    private String friday;

    private String saturday;

    private String sunday;

    @OneToOne(mappedBy = "schedules")
    @JsonIgnore
    private ShelterInfo shelterInfo;

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
