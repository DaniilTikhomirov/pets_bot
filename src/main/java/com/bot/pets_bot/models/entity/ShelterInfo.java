package com.bot.pets_bot.models.entity;


import com.bot.pets_bot.models.dto.SchedulesDTO;
import com.bot.pets_bot.models.dto.ShelterInfoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shelter_info")
@Getter
@Setter
public class ShelterInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "security_contact")
    private String securityContact;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "schema_for_road_photo_url", nullable = false)
    private String schemaForRoadPhotoUrl;

    @Column(name = "safety_precautions")
    private String safetyPrecautions;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "schedules_id")
    private Schedules schedules;

    public static ShelterInfo convertFromDTO(ShelterInfoDTO dto) {
        ShelterInfo shelterInfo = new ShelterInfo();
        shelterInfo.setName(dto.getName());
        shelterInfo.setDescription(dto.getDescription());
        shelterInfo.setSecurityContact(dto.getSecurityContact());
        shelterInfo.setAddress(dto.getAddress());
        shelterInfo.setSchemaForRoadPhotoUrl(dto.getSchemaForRoadPhotoUrl());
        shelterInfo.setSafetyPrecautions(dto.getSafetyPrecautions());
        shelterInfo.setSchedules(Schedules.convertFromDTO(dto.getSchedules()));
        return shelterInfo;
    }
}
