package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.VolunteersDTO;
import com.bot.pets_bot.models.entity.Volunteers;
import com.bot.pets_bot.services.VolunteersService;
import com.bot.pets_bot.repositories.VolunteersRepository;
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

@WebMvcTest(controllers = VolunteersController.class)
public class VolunteersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VolunteersService volunteersService;

    @MockBean
    private VolunteersRepository volunteersRepository;

    @InjectMocks
    private VolunteersController volunteersController;

    private Volunteers generateVolunteer() {
        Volunteers volunteer = new Volunteers();
        volunteer.setId(1L);
        volunteer.setTelegramId(123456789L);
        volunteer.setName("Test Volunteer");
        volunteer.setSecondName("Test SecondName");
        return volunteer;
    }

    private VolunteersDTO generateVolunteerDTO() {
        VolunteersDTO dto = new VolunteersDTO();
        dto.setTelegramId(123456789L);
        dto.setContact("+7-777-777-77-77");
        dto.setName("Test Volunteer");
        dto.setSecondName("Test SecondName");
        return dto;
    }

    @Test
    public void testGetVolunteers() throws Exception {
        Volunteers volunteer = generateVolunteer();
        when(volunteersService.getVolunteers()).thenReturn(List.of(volunteer));

        mockMvc.perform(MockMvcRequestBuilders.get("/bot/volunteers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(volunteer.getName()))
                .andExpect(jsonPath("$[0].telegramId").value(volunteer.getTelegramId()));
    }

    @Test
    public void testAddVolunteer() throws Exception {
        VolunteersDTO volunteerDTO = generateVolunteerDTO();
        Volunteers volunteer = generateVolunteer();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("telegramId", volunteerDTO.getTelegramId());
        jsonObject.put("contact", volunteerDTO.getContact());
        jsonObject.put("name", volunteerDTO.getName());
        jsonObject.put("secondName", volunteerDTO.getSecondName());

        when(volunteersService.addVolunteers(any(VolunteersDTO.class))).thenReturn(volunteer);

        mockMvc.perform(MockMvcRequestBuilders.post("/bot/volunteers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(volunteer.getName()))
                .andExpect(jsonPath("$.telegramId").value(volunteer.getTelegramId()));
    }
}
