package com.bot.pets_bot.models.entity;

import com.bot.pets_bot.models.dto.VolunteersDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Класс, представляющий сущность волонтера.
 * Этот класс используется для отображения данных волонтера в базе данных
 * и содержит информацию о волонтере, связанную с его контактной информацией, именем,
 * а также связями с пользователями Telegram и отчетами.
 */
@Entity
@Table(name = "volunteers")
@Getter
@Setter
public class Volunteers {

    /**
     * Уникальный идентификатор волонтера.
     * Этот идентификатор используется для уникальной идентификации волонтера в системе.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Уникальный идентификатор пользователя в Telegram.
     * Этот идентификатор используется для связи волонтера с его аккаунтом в Telegram.
     */
    @Column(nullable = false, name = "telegram_id")
    private long telegramId;

    /**
     * Контактная информация волонтера.
     * Содержит данные о том, как можно связаться с волонтером.
     */
    @Column(name = "contact")
    private String contact;

    /**
     * Имя волонтера.
     * Хранит имя волонтера в виде текста.
     */
    @Lob
    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    /**
     * Фамилия волонтера.
     * Хранит фамилию волонтера в виде текста.
     */
    @Lob
    @Column(name = "second_name", columnDefinition = "TEXT")
    private String secondName;

    /**
     * Список пользователей Telegram, связанных с этим волонтером.
     * Этот список хранит все объекты пользователей Telegram, с которыми волонтер взаимодействует.
     */
    @OneToMany(mappedBy = "volunteer")
    private List<TelegramUser> users;

    /**
     * Список отчетов, связанных с этим волонтером.
     * Этот список содержит все отчеты, которые были поданы этим волонтером.
     */
    @OneToMany(mappedBy = "volunteers")
    @JsonIgnore
    private List<Reports> reports;

    /**
     * Метод для преобразования объекта VolunteersDTO в объект Volunteers.
     * Используется для конвертации данных из DTO в сущность для сохранения в базу данных.
     *
     * @param dto объект VolunteersDTO, содержащий данные для создания сущности Volunteers.
     * @return сущность Volunteers, созданная на основе данных DTO.
     */
    public static Volunteers convertFromDTO(VolunteersDTO dto) {
        Volunteers volunteers = new Volunteers();
        volunteers.setContact(dto.getContact());
        volunteers.setTelegramId(dto.getTelegramId());
        volunteers.setName(dto.getName());
        volunteers.setSecondName(dto.getSecondName());
        return volunteers;
    }
}
