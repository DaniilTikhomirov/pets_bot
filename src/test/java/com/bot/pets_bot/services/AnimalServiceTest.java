package com.bot.pets_bot.services;

import com.bot.pets_bot.exeptions.BadPage;
import com.bot.pets_bot.exeptions.S3Error;
import com.bot.pets_bot.models.dto.AnimalDTO;
import com.bot.pets_bot.models.entity.Animal;
import com.bot.pets_bot.repositories.AnimalRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

    private Animal animal;
    private AnimalDTO animalDTO;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private AnimalService animalService;

    @BeforeEach
    public void setUp() {
        animal = new Animal();
        animal.setId(1L);
        animal.setName("Buddy");

        animalDTO = new AnimalDTO();
        animalDTO.setName("Buddy");
    }

    @Test
    public void testAddAnimal() {
        when(animalRepository.save(any(Animal.class))).thenReturn(animal);

        Animal savedAnimal = animalService.addAnimal(animalDTO);
        Assertions.assertEquals(animalDTO.getName(), savedAnimal.getName());
        verify(animalRepository, times(1)).save(any(Animal.class));
    }

    @Test
    public void testAddPhoto_Success() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("photo.jpg");
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(s3Service.uploadFile(file, "animal1")).thenReturn(true);
        when(s3Service.getExtension("photo.jpg")).thenReturn("jpg");
        when(animalRepository.save(any(Animal.class))).thenReturn(animal);

        Animal updatedAnimal = animalService.addPhoto(file, 1L);
        Assertions.assertNotNull(updatedAnimal.getPhotoUrl());
        verify(animalRepository, times(1)).findById(1L);
        verify(animalRepository, times(1)).save(any(Animal.class));
    }

    @Test
    public void testAddPhoto_AnimalNotFound() {
        MultipartFile file = mock(MultipartFile.class);
        when(animalRepository.findById(1L)).thenReturn(Optional.empty());

        Animal result = animalService.addPhoto(file, 1L);
        Assertions.assertNull(result);
        verify(animalRepository, times(1)).findById(1L);
        verify(s3Service, times(0)).uploadFile(any(MultipartFile.class), anyString());
    }


    @Test
    public void testGetAllAnimals() {
        when(animalRepository.findAll()).thenReturn(List.of(animal));

        List<Animal> animals = animalService.getAllAnimals();
        Assertions.assertEquals(1, animals.size());
        Assertions.assertEquals(animal.getName(), animals.getFirst().getName());
        verify(animalRepository, times(1)).findAll();
    }

    @Test
    public void testGetAnimalById_Found() {
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));

        Animal foundAnimal = animalService.getAnimalById(1L);
        Assertions.assertNotNull(foundAnimal);
        Assertions.assertEquals(animal.getId(), foundAnimal.getId());
        verify(animalRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAnimalById_NotFound() {
        when(animalRepository.findById(2L)).thenReturn(Optional.empty());

        Animal foundAnimal = animalService.getAnimalById(2L);
        Assertions.assertNull(foundAnimal);
        verify(animalRepository, times(1)).findById(2L);
    }

    @Test
    public void testPutAnimal() {
        when(animalRepository.save(any(Animal.class))).thenReturn(animal);

        Animal updatedAnimal = animalService.putAnimal(animal);
        Assertions.assertEquals(animal.getName(), updatedAnimal.getName());
        verify(animalRepository, times(1)).save(animal);
    }

    @Test
    public void testGetAnimalsPage_ValidPage() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Animal> page = new PageImpl<>(List.of(animal));
        when(animalRepository.findAllByAdopterIsNullAndTakeIsFalseAndCat(pageRequest, false)).thenReturn(page);

        List<Animal> animals = animalService.getAnimalsPage(1, 10, false);
        Assertions.assertEquals(1, animals.size());
        Assertions.assertEquals(animal.getName(), animals.getFirst().getName());
        verify(animalRepository, times(1)).
                findAllByAdopterIsNullAndTakeIsFalseAndCat(pageRequest, false);
    }

    @Test
    public void testGetAnimalsPage_InvalidPage() {
        Assertions.assertThrows(BadPage.class, () -> animalService.getAnimalsPage(0, 10, false));
        verify(animalRepository, times(0)).
                findAllByAdopterIsNullAndTakeIsFalseAndCat(any(PageRequest.class), anyBoolean());
    }
}
