package com.bot.pets_bot.repositories;

import com.bot.pets_bot.models.entity.ShelterInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SheltersInfoRepository extends JpaRepository<ShelterInfo, Long> {
}
