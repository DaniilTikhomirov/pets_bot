package com.bot.pets_bot.services;

import com.bot.pets_bot.models.dto.AnimalsAdviceDTO;
import com.bot.pets_bot.models.entity.AnimalsAdvice;
import com.bot.pets_bot.models.entity.DogHandler;
import com.bot.pets_bot.repositories.AnimalsAdviceRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimalAdviceServiceTest {

    private AnimalsAdvice animalsAdvice;

    private AnimalsAdviceDTO animalsAdviceDTO;

    @BeforeEach
    public void setUp() {
        animalsAdvice = new AnimalsAdvice();
        animalsAdvice.setId(1L);
        animalsAdvice.setRulesForGettingAnimal("Advice");

        animalsAdviceDTO = new AnimalsAdviceDTO();
        animalsAdviceDTO.setRulesForGettingAnimal("Advice");
    }

    @Mock
    private AnimalsAdviceRepository animalsAdviceRepository;

    @InjectMocks
    private AnimalAdviceService animalAdviceService;

    @Test
    public void testAddAnimalAdvice() {
        when(animalsAdviceRepository.save(any(AnimalsAdvice.class))).thenReturn(animalsAdvice);

        AnimalsAdvice savedAdvice = animalAdviceService.addAnimalAdvice(animalsAdviceDTO);
        Assertions.assertEquals(animalsAdviceDTO.getRulesForGettingAnimal(), savedAdvice.getRulesForGettingAnimal());
    }

    @Test
    public void testGetAllAnimalAdvice() {
        when(animalsAdviceRepository.findAll()).thenReturn(List.of(animalsAdvice));

        List<AnimalsAdvice> adviceList = animalAdviceService.getAllAnimalAdvice();
        Assertions.assertEquals(1, adviceList.size());
        Assertions.assertEquals(animalsAdvice.getRulesForGettingAnimal(), adviceList.getFirst().getRulesForGettingAnimal());
    }

    @Test
    public void testPutAnimalAdvice() {
        DogHandler handler = new DogHandler();
        handler.setId(1L);
        animalsAdvice.setDogHandlers(List.of(handler));

        when(animalsAdviceRepository.save(any(AnimalsAdvice.class))).thenReturn(animalsAdvice);

        AnimalsAdvice updatedAdvice = animalAdviceService.putAnimalAdvice(animalsAdvice);

        Assertions.assertEquals(animalsAdvice.getRulesForGettingAnimal(), updatedAdvice.getRulesForGettingAnimal());
        verify(animalsAdviceRepository, times(1)).save(animalsAdvice);
    }

    @Test
    public void testGetAnimalAdviceById() {
        when(animalsAdviceRepository.findById(1L)).thenReturn(Optional.of(animalsAdvice));

        AnimalsAdvice foundAdvice = animalAdviceService.getAnimalAdviceById(1L);
        Assertions.assertNotNull(foundAdvice);
        Assertions.assertEquals(animalsAdvice.getId(), foundAdvice.getId());
    }

    @Test
    public void testGetAnimalAdviceById_NotFound() {
        when(animalsAdviceRepository.findById(2L)).thenReturn(Optional.empty());

        AnimalsAdvice foundAdvice = animalAdviceService.getAnimalAdviceById(2L);
        Assertions.assertNull(foundAdvice);
    }
}
