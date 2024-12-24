package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.OwnerDTO;
import com.bot.pets_bot.models.entity.Owner;
import com.bot.pets_bot.repositories.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с владельцами животных.
 * Предоставляет методы для добавления владельцев и получения всех владельцев.
 */
@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    /**
     * Конструктор сервиса для внедрения зависимости репозитория владельцев.
     *
     * @param ownerRepository Репозиторий для работы с сущностями Owner (владельцы).
     */
    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    /**
     * Добавляет нового владельца.
     * Преобразует объект DTO в сущность Owner и сохраняет его в базе данных.
     *
     * @param owner DTO объект владельца.
     * @return Сохраненная сущность Owner.
     */
    public Owner addOwner(OwnerDTO owner) {
        return ownerRepository.save(Owner.convertFromDTO(owner));
    }

    /**
     * Возвращает список всех владельцев.
     *
     * @return Список всех владельцев.
     */
    public List<Owner> getAllOwners() {
        return ownerRepository.findAll();
    }
}
