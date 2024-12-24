package com.bot.pets_bot.models.entity;

import com.bot.pets_bot.models.dto.OwnerDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность для владельца животного.
 * Содержит информацию о владельце, такую как имя и идентификатор Telegram.
 */
@Entity
@Table(name = "owners")
@Getter
@Setter
public class Owner {

    /**
     * Уникальный идентификатор владельца.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Идентификатор Telegram владельца.
     * Он обязателен и используется для связи с пользователем.
     */
    @Column(name = "telegram_id", nullable = false)
    private long telegramId;

    /**
     * Имя владельца.
     * Хранится в формате текстового значения.
     */
    @Lob
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    /**
     * Преобразует объект DTO в сущность владельца.
     * @param dto объект {@link OwnerDTO}, содержащий данные владельца.
     * @return новый объект {@link Owner} с данными из DTO.
     */
    public static Owner convertFromDTO(OwnerDTO dto) {
        Owner owner = new Owner();
        owner.setTelegramId(dto.getTelegramId());
        owner.setName(dto.getName());

        return owner;
    }
}
