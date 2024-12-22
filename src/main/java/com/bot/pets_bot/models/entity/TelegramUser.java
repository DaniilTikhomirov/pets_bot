package com.bot.pets_bot.models.entity;

import com.bot.pets_bot.models.dto.TelegramUserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Класс, представляющий пользователя Telegram в системе.
 * Этот класс используется для хранения информации о пользователе Telegram,
 * который взаимодействует с ботом, включая идентификатор, контактные данные и количество предупреждений.
 */
@Entity
@Table(name = "telegram_users")
@Getter
@Setter
public class TelegramUser {

    /**
     * Уникальный идентификатор пользователя в базе данных.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Идентификатор пользователя в Telegram.
     * Это уникальное значение, которое используется для идентификации пользователя в системе.
     */
    @Column(nullable = false, name = "telegram_id", unique = true)
    private long telegramId;

    /**
     * Контактная информация пользователя.
     * Это может быть телефонный номер, email или другие контактные данные.
     */
    @Column(name = "contact")
    private String contact;

    /**
     * Счетчик предупреждений, полученных пользователем.
     * Если счетчик превышает определенное количество, могут быть предприняты действия,
     * такие как блокировка пользователя или другие ограничения.
     */
    @Column(name = "warning_counter", columnDefinition = "int default (0)")
    private int warningCounter;

    /**
     * Имя пользователя Telegram.
     */
    @Column(name = "name")
    private String name;

    /**
     * Взаимосвязь с объектом волонтера.
     * Каждый пользователь Telegram может быть связан с волонтером,
     * если они взаимодействуют в контексте приюта.
     */
    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    @JsonIgnore
    private Volunteers volunteer;

    /**
     * Преобразует объект DTO в сущность.
     *
     * @param dto объект TelegramUserDTO
     * @return объект TelegramUser
     */
    public static TelegramUser convertFromDTO(TelegramUserDTO dto) {
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setContact(dto.getContact());
        telegramUser.setTelegramId(dto.getTelegramId());
        telegramUser.setName(dto.getName());
        return telegramUser;
    }
}
