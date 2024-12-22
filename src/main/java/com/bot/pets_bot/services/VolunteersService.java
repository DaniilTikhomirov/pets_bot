package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.VolunteersDTO;
import com.bot.pets_bot.models.entity.Volunteers;
import com.bot.pets_bot.repositories.VolunteersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * Сервис для работы с волонтерами.
 * Предоставляет методы для добавления волонтеров, получения списка всех волонтеров,
 * получения случайного волонтера и поиска волонтера по ID.
 */
@Service
public class VolunteersService {

    private final VolunteersRepository volunteersRepository;

    /**
     * Конструктор сервиса для инициализации репозитория волонтеров.
     *
     * @param volunteersRepository Репозиторий для работы с волонтерами.
     */
    public VolunteersService(VolunteersRepository volunteersRepository) {
        this.volunteersRepository = volunteersRepository;
    }

    /**
     * Получает список всех волонтеров.
     *
     * @return Список объектов Volunteers.
     */
    public List<Volunteers> getVolunteers() {
        return volunteersRepository.findAll();
    }

    /**
     * Добавляет нового волонтера.
     *
     * @param dto Объект DTO, содержащий информацию о волонтере.
     * @return Сохраненный объект Volunteers.
     */
    public Volunteers addVolunteers(VolunteersDTO dto) {
        Volunteers volunteers = Volunteers.convertFromDTO(dto);
        return volunteersRepository.save(volunteers);
    }

    /**
     * Получает случайного волонтера из списка всех волонтеров.
     *
     * @return Случайный объект Volunteers или null, если список волонтеров пуст.
     */
    public Volunteers getRandomVolunteer() {
        List<Volunteers> volunteers = getVolunteers();
        if (volunteers.isEmpty()) return null;
        Random random = new Random();
        return volunteers.get(random.nextInt(volunteers.size()));
    }

    /**
     * Получает волонтера по его ID.
     *
     * @param id Идентификатор волонтера.
     * @return Объект Volunteers или null, если волонтер не найден.
     */
    public Volunteers getVolunteersById(long id) {
        return volunteersRepository.findById(id).orElse(null);
    }
}
