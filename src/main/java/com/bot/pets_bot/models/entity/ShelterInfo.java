package com.bot.pets_bot.models.entity;

import com.bot.pets_bot.models.dto.ShelterInfoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность, представляющая информацию о приюте для животных.
 * Этот класс содержит информацию о приюте, такую как имя, описание, контактную информацию безопасности, адрес,
 * схему проезда, меры безопасности и расписание работы.
 */
@Entity
@Table(name = "shelter_info")
@Getter
@Setter
public class ShelterInfo {

    /**
     * Идентификатор приюта.
     * Уникальный идентификатор для каждого приюта.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Имя приюта.
     * Название приюта для отображения в интерфейсе.
     */
    @Lob
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    /**
     * Описание приюта.
     * Краткое описание приюта, его миссия и услуги.
     */
    @Lob
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    /**
     * Контактная информация для безопасности.
     * Номер телефона или другие контактные данные для связи в случае экстренных ситуаций.
     */
    @Column(name = "security_contact")
    private String securityContact;

    /**
     * Адрес приюта.
     * Адрес приюта, где его можно найти.
     */
    @Lob
    @Column(name = "address", nullable = false, columnDefinition = "TEXT")
    private String address;

    /**
     * Схема проезда к приюту.
     * URL-ссылка на фотографию или схему, которая помогает добраться до приюта.
     */
    @Lob
    @Column(name = "schema_for_road_photo_url", nullable = false, columnDefinition = "TEXT")
    private String schemaForRoadPhotoUrl;

    /**
     * Меры безопасности в приюте.
     * Описание правил и мер безопасности, которые следует соблюдать при посещении приюта.
     */
    @Lob
    @Column(name = "safety_precautions", columnDefinition = "TEXT")
    private String safetyPrecautions;

    /**
     * Расписание работы приюта.
     * Содержит расписание работы приюта для каждого дня недели.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "schedules_id")
    private Schedules schedules;

    /**
     * Преобразует объект DTO в сущность {@link ShelterInfo}.
     *
     * @param dto объект DTO, содержащий информацию о приюте.
     * @return сущность {@link ShelterInfo}, заполненная данными из DTO.
     */
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
