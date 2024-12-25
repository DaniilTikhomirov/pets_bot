package com.bot.pets_bot.repositories;

import com.bot.pets_bot.models.entity.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Page<Animal> findAllByAdopterIsNullAndTakeIsFalseAndCat(Pageable pageable, boolean cat);
}
