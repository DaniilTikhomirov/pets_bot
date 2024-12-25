package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.ShelterInfoDTO;
import com.bot.pets_bot.models.entity.ShelterInfo;
import com.bot.pets_bot.services.ShelterInfoService;
import com.bot.pets_bot.repositories.SheltersInfoRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(controllers = SheltersInfoController.class)
public class SheltersInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShelterInfoService shelterInfoService;

    @MockBean
    private SheltersInfoRepository shelterInfoRepository;

    @InjectMocks
    private SheltersInfoController sheltersInfoController;

    private ShelterInfo generateShelterInfo() {
        ShelterInfo shelterInfo = new ShelterInfo();
        shelterInfo.setId(1L);
        shelterInfo.setName("Test Shelter");
        return shelterInfo;
    }

    private ShelterInfoDTO generateShelterInfoDTO() {
        ShelterInfoDTO dto = new ShelterInfoDTO();
        dto.setName("Test Shelter");
        return dto;
    }

    @Test
    public void testGetShelters() throws Exception {
        ShelterInfo shelterInfo = generateShelterInfo();
        when(shelterInfoService.getSheltersInfo()).thenReturn(List.of(shelterInfo));

        mockMvc.perform(MockMvcRequestBuilders.get("/bot/shelters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(shelterInfo.getName()));
    }

    @Test
    public void testAddShelter() throws Exception {
        ShelterInfoDTO shelterInfoDTO = generateShelterInfoDTO();
        ShelterInfo shelterInfo = generateShelterInfo();

        JSONObject schedules = new JSONObject();
        schedules.put("monday", "test");
        schedules.put("tuesday", "test");
        schedules.put("wednesday", "test");
        schedules.put("thursday", "test");
        schedules.put("friday", "test");
        schedules.put("saturday", "test");
        schedules.put("sunday", "test");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", shelterInfoDTO.getName());
        jsonObject.put("description", "test");
        jsonObject.put("securityContact", "+7-777-777-77-77");
        jsonObject.put("address", "test");
        jsonObject.put("schemaForRoadPhotoUrl", "test");
        jsonObject.put("safetyPrecautions", "test");
        jsonObject.put("schedules", schedules);

        when(shelterInfoService.addShelterInfo(any(ShelterInfoDTO.class))).thenReturn(shelterInfo);

        mockMvc.perform(MockMvcRequestBuilders.post("/bot/shelters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(shelterInfo.getName()));
    }
}
