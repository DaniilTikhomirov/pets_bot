package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.AdopterDTO;
import com.bot.pets_bot.models.entity.Adopter;
import com.bot.pets_bot.repositories.AdopterRepository;
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
public class AdopterServiceTest {

    private Adopter adopter;

    private AdopterDTO adopterDTO;

    @BeforeEach
    public void setUp() {
        adopter = new Adopter();
        adopter.setId(1);
        adopter.setName("John Doe");
        adopter.setTelegramId(123456789L);

        adopterDTO = new AdopterDTO();
        adopterDTO.setName("John Doe");
        adopterDTO.setTelegramId(123456789L);
    }

    @Mock
    private AdopterRepository adopterRepository;

    @InjectMocks
    private AdopterService adopterService;

    @Test
    public void testAddAdopter() {
        when(adopterRepository.save(any(Adopter.class))).thenReturn(adopter);

        Adopter savedAdopter = adopterService.addAdopter(adopterDTO);
        Assertions.assertEquals(adopterDTO.getName(), savedAdopter.getName());
        Assertions.assertEquals(adopterDTO.getTelegramId(), savedAdopter.getTelegramId());
    }

    @Test
    public void testPutAdopter() {
        when(adopterRepository.save(any(Adopter.class))).thenReturn(adopter);

        Adopter updatedAdopter = adopterService.putAdopter(adopter);
        Assertions.assertEquals(adopter.getName(), updatedAdopter.getName());
        Assertions.assertEquals(adopter.getTelegramId(), updatedAdopter.getTelegramId());
    }

    @Test
    public void testGetAllAdopters() {
        when(adopterRepository.findAll()).thenReturn(List.of(adopter));

        List<Adopter> adopters = adopterService.getAllAdopters();
        Assertions.assertEquals(1, adopters.size());
        Assertions.assertEquals(adopter.getId(), adopters.get(0).getId());
    }

    @Test
    public void testFindAdopterByTelegramId() {
        when(adopterRepository.findFirstByTelegramId(123456789L)).thenReturn(Optional.of(adopter));

        Adopter foundAdopter = adopterService.findAdopterByTelegramId(123456789L);
        Assertions.assertNotNull(foundAdopter);
        Assertions.assertEquals(adopter.getTelegramId(), foundAdopter.getTelegramId());
    }

    @Test
    public void testFindAdopterByTelegramId_NotFound() {
        when(adopterRepository.findFirstByTelegramId(987654321L)).thenReturn(Optional.empty());

        Adopter foundAdopter = adopterService.findAdopterByTelegramId(987654321L);
        Assertions.assertNull(foundAdopter);
    }

    @Test
    public void testGetAdopterById() {
        when(adopterRepository.findById(1L)).thenReturn(Optional.of(adopter));

        Adopter foundAdopter = adopterService.getAdopterById(1L);
        Assertions.assertNotNull(foundAdopter);
        Assertions.assertEquals(adopter.getId(), foundAdopter.getId());
    }

    @Test
    public void testGetAdopterById_NotFound() {
        when(adopterRepository.findById(2L)).thenReturn(Optional.empty());

        Adopter foundAdopter = adopterService.getAdopterById(2L);
        Assertions.assertNull(foundAdopter);
    }
}
