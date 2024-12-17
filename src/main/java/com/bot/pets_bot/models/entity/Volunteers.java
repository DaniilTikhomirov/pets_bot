package com.bot.pets_bot.models.entity;

import com.bot.pets_bot.models.dto.VolunteersDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "volunteers")
@Getter
@Setter
public class Volunteers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, name = "telegram_id")
    private long telegramId;

    @Column(name = "contact")
    private String contact;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "volunteer")
    private List<TelegramUser> users;

    public static Volunteers convertFromDTO(VolunteersDTO dto) {
        Volunteers volunteers = new Volunteers();
        volunteers.setContact(dto.getContact());
        volunteers.setTelegramId(dto.getTelegramId());
        volunteers.setName(dto.getName());
        return volunteers;
    }
}
