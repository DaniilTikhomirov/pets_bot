package com.bot.pets_bot.controllers.api;


import com.bot.pets_bot.models.dto.AdopterDTO;
import com.bot.pets_bot.models.entity.Adopter;
import com.bot.pets_bot.repositories.AdopterRepository;
import com.bot.pets_bot.services.AdopterService;
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

@WebMvcTest(controllers = AdopterController.class)
public class AdopterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdopterRepository adopterRepository;

    @SpyBean
    private AdopterService adopterService;

    @InjectMocks
    private AdopterController adopterController;

    private Adopter generateAdopter() {
        Adopter adopter = new Adopter();
        adopter.setId(1);
        adopter.setName("Adopter");
        adopter.setContact("+7-777-777-77-77");
        adopter.setTelegramId(5678);

        return adopter;
    }

    private AdopterDTO getAdopterDTO() {
        AdopterDTO adopterDTO = new AdopterDTO();
        adopterDTO.setName("Adopter");
        adopterDTO.setContact("+7-777-777-77-77");
        adopterDTO.setTelegramId(5678);

        return adopterDTO;
    }

    private JSONObject getJSObject() throws JSONException {
        JSONObject jsObject = new JSONObject();
        jsObject.put("name", "Adopter");
        jsObject.put("contact", "+7-777-777-77-77");
        jsObject.put("telegramId", 5678);

        return jsObject;
    }


    @Test
    public void testAddAdopter() throws Exception {
        AdopterDTO adopterDTO = getAdopterDTO();
        JSONObject jsObject = getJSObject();

        when(adopterRepository.save(any(Adopter.class))).thenReturn(generateAdopter());

        mockMvc.perform(MockMvcRequestBuilders.post("/bot/adopter").
                content(jsObject.toString()).
                contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.contact").value(adopterDTO.getContact())).
                andExpect(jsonPath("$.name").value(adopterDTO.getName()));
    }

    @Test
    public void testGetAdopters() throws Exception {
        when(adopterRepository.findAll()).thenReturn(List.of(generateAdopter()));

        mockMvc.perform(MockMvcRequestBuilders.get("/bot/adopter")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$[0].name").value(generateAdopter().getName()));
    }
}