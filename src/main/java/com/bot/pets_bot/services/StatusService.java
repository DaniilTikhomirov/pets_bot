package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.StatusDTO;
import com.bot.pets_bot.models.entity.Status;
import com.bot.pets_bot.repositories.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с состояниями.
 * Предоставляет методы для добавления и получения статусов.
 */
@Service
public class StatusService {

    private final StatusRepository statusRepository;

    /**
     * Конструктор сервиса для инициализации репозитория.
     *
     * @param statusRepository Репозиторий для работы с данными статусов.
     */
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    /**
     * Добавляет новый статус в базу данных.
     *
     * @param status Объект DTO, содержащий информацию о статусе.
     * @return Сохраненный объект Status.
     */
    public Status addStatus(StatusDTO status) {
        return statusRepository.save(Status.convertFromDTO(status));
    }

    /**
     * Получает список всех статусов.
     *
     * @return Список объектов Status.
     */
    public List<Status> getAllStatus() {
        return statusRepository.findAll();
    }
}
