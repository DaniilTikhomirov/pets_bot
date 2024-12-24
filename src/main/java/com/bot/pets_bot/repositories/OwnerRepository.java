package com.bot.pets_bot.repositories;

import com.bot.pets_bot.models.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
