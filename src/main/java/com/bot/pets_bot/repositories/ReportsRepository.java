package com.bot.pets_bot.repositories;

import com.bot.pets_bot.models.entity.Reports;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportsRepository extends JpaRepository<Reports, Long> {
}
