package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.AnimalsAdviceDTO;
import com.bot.pets_bot.models.entity.AnimalsAdvice;
import com.bot.pets_bot.services.AnimalAdviceService;
import com.bot.pets_bot.repositories.AnimalsAdviceRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AnimalAdviceController.class)
public class AnimalAdviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private AnimalAdviceService animalAdviceService;

    @MockBean
    private AnimalsAdviceRepository animalsAdviceRepository;

    @InjectMocks
    private AnimalAdviceController animalAdviceController;

    private AnimalsAdvice generateAnimalAdvice() {
        AnimalsAdvice animalsAdvice = new AnimalsAdvice();
        animalsAdvice.setId(1);
        animalsAdvice.setRulesForGettingAnimal("Rules");

        return animalsAdvice;
    }

    private AnimalsAdviceDTO getAnimalsAdviceDTO() {
        AnimalsAdviceDTO animalsAdviceDTO = new AnimalsAdviceDTO();
        animalsAdviceDTO.setRulesForGettingAnimal("Rules");

        return animalsAdviceDTO;
    }

    private JSONObject getJSObject() throws JSONException {
        JSONObject jsObject = new JSONObject();
        jsObject.put("rulesForGettingAnimal", "Rules");

        return jsObject;
    }

    @Test
    public void testAddAnimalAdvice() throws Exception {
        AnimalsAdviceDTO animalsAdviceDTO = getAnimalsAdviceDTO();
        JSONObject jsObject = getJSObject();

        when(animalsAdviceRepository.save(any(AnimalsAdvice.class))).thenReturn(generateAnimalAdvice());

        mockMvc.perform(MockMvcRequestBuilders.post("/bot/animal_advice")
                        .content(jsObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rulesForGettingAnimal").value(animalsAdviceDTO.getRulesForGettingAnimal()));
    }

    @Test
    public void testGetAnimalAdvices() throws Exception {
        when(animalsAdviceRepository.findAll()).thenReturn(List.of(generateAnimalAdvice()));

        mockMvc.perform(MockMvcRequestBuilders.get("/bot/animal_advice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rulesForGettingAnimal").value(generateAnimalAdvice().getRulesForGettingAnimal()));
    }

    @Test
    public void testPutAnimalAdvice() throws Exception {
        AnimalsAdvice animalsAdvice = generateAnimalAdvice();
        JSONObject jsObject = getJSObject();

        when(animalsAdviceRepository.save(any(AnimalsAdvice.class))).thenReturn(animalsAdvice);

        mockMvc.perform(MockMvcRequestBuilders.put("/bot/animal_advice")
                        .content(jsObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rulesForGettingAnimal").value(animalsAdvice.getRulesForGettingAnimal()));
    }
}
