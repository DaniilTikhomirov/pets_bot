package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.VolunteersDTO;
import com.bot.pets_bot.models.entity.Volunteers;
import com.bot.pets_bot.repositories.VolunteersRepository;
import org.apache.http.util.Asserts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VolunteersServiceTest {

    private Volunteers volunteers;

    private VolunteersDTO volunteersDTO;

    @BeforeEach
    void setUp() {
        volunteers = new Volunteers();
        volunteers.setId(1L);
        volunteers.setName("Volunteers");

        volunteersDTO = new VolunteersDTO();
        volunteersDTO.setName("Volunteers");
    }

    @Mock
    private VolunteersRepository volunteersRepository;

    @InjectMocks
    private VolunteersService volunteersService;

    @Test
    public void getAllVolunteers() {
        when(volunteersRepository.findAll()).thenReturn(List.of(volunteers));

        List<Volunteers> volunteers1 = volunteersService.getVolunteers();

        Assertions.assertEquals(volunteers.getName(), volunteers1.getFirst().getName());
    }

    @Test
    public void getVolunteerById() {
        when(volunteersRepository.findById(any(Long.class))).thenReturn(Optional.of(volunteers));

        Volunteers volunteers1 = volunteersService.getVolunteersById(volunteers.getId());

        Assertions.assertEquals(volunteers.getName(), volunteers1.getName());

    }

    @Test
    public void createVolunteer() {
        when(volunteersRepository.save(any(Volunteers.class))).thenReturn(volunteers);

        Volunteers volunteers1 = volunteersService.addVolunteers(volunteersDTO);

        Assertions.assertEquals(volunteersDTO.getName(), volunteers1.getName());
    }
}
