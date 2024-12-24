package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.ShelterInfoDTO;
import com.bot.pets_bot.models.entity.ShelterInfo;
import com.bot.pets_bot.repositories.SheltersInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с информацией о приютах.
 * Предоставляет методы для добавления, получения и управления информацией о приютах.
 */
@Service
public class ShelterInfoService {
    private final SheltersInfoRepository shelterInfoRepository;

    /**
     * Конструктор сервиса для инициализации репозитория.
     *
     * @param shelterInfoRepository Репозиторий для работы с данными приютов.
     */
    public ShelterInfoService(SheltersInfoRepository shelterInfoRepository) {
        this.shelterInfoRepository = shelterInfoRepository;
    }

    /**
     * Получает список всех приютов.
     *
     * @return Список объектов ShelterInfo.
     */
    public List<ShelterInfo> getSheltersInfo() {
        return shelterInfoRepository.findAll();
    }

    /**
     * Добавляет новый объект информации о приюте.
     *
     * @param dto Объект DTO, содержащий информацию о приюте.
     * @return Сохраненный объект ShelterInfo.
     */
    public ShelterInfo addShelterInfo(ShelterInfoDTO dto) {
        ShelterInfo shelterInfo = ShelterInfo.convertFromDTO(dto);
        return shelterInfoRepository.save(shelterInfo);
    }

    /**
     * Получает информацию о приюте по его идентификатору.
     *
     * @param id Идентификатор приюта.
     * @return Объект ShelterInfo, если приют найден, иначе null.
     */
    public ShelterInfo getShelterInfoById(long id) {
        return shelterInfoRepository.findById(id).orElse(null);
    }
}
