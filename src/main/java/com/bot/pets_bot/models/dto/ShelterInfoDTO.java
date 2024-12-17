package com.bot.pets_bot.models.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShelterInfoDTO {

    private String name;

    private String description;

    private String securityContact;

    private String address;

    private String schemaForRoadPhotoUrl;

    private String safetyPrecautions;

    private SchedulesDTO schedules;

}
