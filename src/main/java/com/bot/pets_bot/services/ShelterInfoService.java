package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.ShelterInfoDTO;
import com.bot.pets_bot.models.entity.ShelterInfo;
import com.bot.pets_bot.repositories.SheltersInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShelterInfoService {
    private final SheltersInfoRepository shelterInfoRepository;

    public ShelterInfoService(SheltersInfoRepository shelterInfoRepository) {
        this.shelterInfoRepository = shelterInfoRepository;
    }

    public List<ShelterInfo> getSheltersInfo() {
        return shelterInfoRepository.findAll();
    }

    public ShelterInfo addShelterInfo(ShelterInfoDTO dto) {
        ShelterInfo shelterInfo = ShelterInfo.convertFromDTO(dto);
        return shelterInfoRepository.save(shelterInfo);
    }
}
