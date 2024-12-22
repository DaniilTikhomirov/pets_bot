package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.AnimalsAdviceDTO;
import com.bot.pets_bot.models.entity.AnimalsAdvice;
import com.bot.pets_bot.repositories.AnimalsAdviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с данными советов по животным (AnimalsAdvice).
 * Предоставляет методы для добавления, обновления, получения и поиска советов по животным.
 */
@Service
public class AnimalAdviceService {
    private final AnimalsAdviceRepository animalsAdviceRepository;

    /**
     * Конструктор сервиса для внедрения зависимостей.
     *
     * @param animalsAdviceRepository Репозиторий для работы с сущностью AnimalsAdvice.
     */
    public AnimalAdviceService(AnimalsAdviceRepository animalsAdviceRepository) {
        this.animalsAdviceRepository = animalsAdviceRepository;
    }

    /**
     * Добавляет новый совет по животным в базу данных.
     *
     * @param animalsAdvice DTO объекта совета по животным, который нужно добавить.
     * @return Сущность AnimalsAdvice, сохраненная в базе данных.
     */
    public AnimalsAdvice addAnimalAdvice(AnimalsAdviceDTO animalsAdvice) {
        return animalsAdviceRepository.save(AnimalsAdvice.convertFromDTO(animalsAdvice));
    }

    /**
     * Возвращает список всех советов по животным.
     *
     * @return Список всех советов по животным (List).
     */
    public List<AnimalsAdvice> getAllAnimalAdvice() {
        return animalsAdviceRepository.findAll();
    }

    /**
     * Обновляет данные существующего совета по животным.
     * Для каждого обработчика собаки (DogHandler) устанавливает текущий совет.
     *
     * @param animalsAdvice Сущность AnimalsAdvice, которая будет обновлена в базе данных.
     * @return Обновленная сущность AnimalsAdvice.
     */
    public AnimalsAdvice putAnimalAdvice(AnimalsAdvice animalsAdvice) {
        animalsAdvice.getDogHandlers().forEach(o -> {
            o.setAdvice(animalsAdvice);
        });
        return animalsAdviceRepository.save(animalsAdvice);
    }

    /**
     * Ищет совет по животным по его ID.
     *
     * @param id ID совета по животным.
     * @return Сущность AnimalsAdvice, если такой совет найден, иначе null.
     */
    public AnimalsAdvice getAnimalAdviceById(Long id) {
        return animalsAdviceRepository.findById(id).orElse(null);
    }
}
