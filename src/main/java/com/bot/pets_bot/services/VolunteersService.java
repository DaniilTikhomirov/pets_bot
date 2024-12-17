package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.VolunteersDTO;
import com.bot.pets_bot.models.entity.Volunteers;
import com.bot.pets_bot.repositories.VolunteersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolunteersService {

    private final VolunteersRepository volunteersRepository;

    public VolunteersService(VolunteersRepository volunteersRepository) {
        this.volunteersRepository = volunteersRepository;
    }

    public List<Volunteers> getVolunteers(){
        return volunteersRepository.findAll();
    }

    public Volunteers addVolunteers(VolunteersDTO dto){
        Volunteers volunteers = Volunteers.convertFromDTO(dto);
        return volunteersRepository.save(volunteers);
    }
}
