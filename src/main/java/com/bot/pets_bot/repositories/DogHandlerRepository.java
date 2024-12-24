package com.bot.pets_bot.repositories;

import com.bot.pets_bot.models.entity.DogHandler;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogHandlerRepository extends JpaRepository<DogHandler, Long> {

}
