package com.bot.pets_bot.models.entity;


import com.bot.pets_bot.models.dto.TelegramUserDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "telegram_users")
@Getter
@Setter
public class TelegramUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, name = "telegram_id")
    private long telegramId;

    @Column(name = "contact")
    private String contact;

    @Column(name = "warning_counter", columnDefinition = "int default (0)")
    private int warningCounter;

    @Column(name = "streak_counter", columnDefinition = "int default (0)")
    private int streakCounter;

    @ManyToOne
    @JoinColumn(name = "volunteer_id")
    @JsonIgnore
    private Volunteers volunteer;

    public static TelegramUser convertFromDTO(TelegramUserDTO dto) {
        TelegramUser telegramUser = new TelegramUser();
        telegramUser.setContact(dto.getContact());
        telegramUser.setTelegramId(dto.getTelegramId());
        return telegramUser;
    }
}
