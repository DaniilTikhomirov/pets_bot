package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.AdopterDTO;
import com.bot.pets_bot.models.entity.Adopter;
import com.bot.pets_bot.repositories.AdopterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для работы с данными усыновителей (Adopter).
 * Предоставляет методы для добавления, обновления, получения и поиска усыновителей по различным критериям.
 */
@Service
public class AdopterService {

    private final AdopterRepository adopterRepository;

    /**
     * Конструктор сервиса для внедрения зависимостей.
     *
     * @param adopterRepository Репозиторий для работы с сущностью Adopter.
     */
    public AdopterService(AdopterRepository adopterRepository) {
        this.adopterRepository = adopterRepository;
    }

    /**
     * Добавляет нового усыновителя в базу данных.
     *
     * @param adopter DTO объекта усыновителя, который нужно добавить.
     * @return Сущность Adopter, сохраненная в базе данных.
     */
    public Adopter addAdopter(AdopterDTO adopter) {
        return adopterRepository.save(Adopter.convertFromDTO(adopter));
    }

    /**
     * Обновляет данные существующего усыновителя.
     *
     * @param adopter Сущность Adopter, которая будет обновлена в базе данных.
     * @return Обновленная сущность Adopter.
     */
    public Adopter putAdopter(Adopter adopter) {
        return adopterRepository.save(adopter);
    }

    /**
     * Возвращает список всех усыновителей.
     *
     * @return Список всех усыновителей (List).
     */
    public List<Adopter> getAllAdopters() {
        return adopterRepository.findAll();
    }

    /**
     * Ищет усыновителя по его Telegram ID.
     * Этот метод помечен как транзакционный, так как возможно потребуется дополнительная логика,
     * связанная с транзакциями, в будущем.
     *
     * @param id Telegram ID усыновителя.
     * @return Сущность Adopter, если такой усыновитель найден, иначе null.
     */
    @Transactional
    public Adopter findAdopterByTelegramId(long id) {
        return adopterRepository.findFirstByTelegramId(id).orElse(null);
    }

    /**
     * Ищет усыновителя по его ID.
     *
     * @param id ID усыновителя.
     * @return Сущность Adopter, если такой усыновитель найден, иначе null.
     */
    public Adopter getAdopterById(long id){
        return adopterRepository.findById(id).orElse(null);
    }
}
