package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.AnimalDTO;
import com.bot.pets_bot.models.entity.Animal;
import com.bot.pets_bot.repositories.AnimalRepository;
import com.bot.pets_bot.services.AnimalService;
import com.bot.pets_bot.services.S3Service;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnimalController.class)
public class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private AnimalService animalService;

    @MockBean
    private AnimalRepository animalRepository;

    @MockBean
    private S3Service s3Service;

    private Animal generateAnimal() {
        Animal animal = new Animal();
        animal.setId(1L);
        animal.setDescription("A lovely cat");
        animal.setAge(2);
        return animal;
    }

    private AnimalDTO getAnimalDTO() {
        AnimalDTO animalDTO = new AnimalDTO();
        animalDTO.setDescription("A lovely cat");
        animalDTO.setAge(2);
        return animalDTO;
    }

    private JSONObject getAnimalDTOJson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("description", "A lovely cat");
        jsonObject.put("age", 2);
        return jsonObject;
    }

    @Test
    public void testAddAnimal() throws Exception {
        AnimalDTO animalDTO = getAnimalDTO();
        JSONObject jsonObject = getAnimalDTOJson();

        when(animalRepository.save(any(Animal.class))).thenReturn(generateAnimal());

        mockMvc.perform(post("/bot/animals")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(animalDTO.getDescription()))
                .andExpect(jsonPath("$.age").value(animalDTO.getAge()));
    }

    @Test
    public void testAddAnimalPhoto() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "cat.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image".getBytes()
        );

        Animal animal = generateAnimal();
        animal.setPhotoUrl("photo_url");

        when(s3Service.uploadFile(any(MultipartFile.class), anyString())).thenReturn(true);
        when(animalRepository.findById(anyLong())).thenReturn(java.util.Optional.of(generateAnimal()));
        when(animalRepository.save(any(Animal.class))).thenReturn(animal);

        mockMvc.perform(multipart("/bot/animals/add_photo/1")
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.photoUrl").value("photo_url"));
    }

    @Test
    public void testGetAnimalPage() throws Exception {
        Animal animal = generateAnimal();

        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<Animal> page = new PageImpl<>(Collections.singletonList(animal));

        when(animalRepository.findAllByAdopterIsNullAndTakeIsFalseAndCat(pageRequest, false)).
                thenReturn(page);

        mockMvc.perform(get("/bot/animals/get_page")
                        .param("page", "1")
                        .param("size", "10")
                        .param("cat", "false")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value(animal.getDescription()))
                .andExpect(jsonPath("$[0].age").value(animal.getAge()));
    }

    @Test
    public void testGetAllAnimals() throws Exception {
        Animal animal = generateAnimal();

        when(animalRepository.findAll()).thenReturn(List.of(animal));
        when(animalService.getAllAnimals()).thenReturn(List.of(animal));

        mockMvc.perform(get("/bot/animals")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value(animal.getDescription()))
                .andExpect(jsonPath("$[0].age").value(animal.getAge()));
    }
}
