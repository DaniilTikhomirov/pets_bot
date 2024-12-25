package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.OwnerDTO;
import com.bot.pets_bot.models.entity.Owner;
import com.bot.pets_bot.repositories.OwnerRepository;
import com.bot.pets_bot.services.OwnerService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OwnersController.class)
public class OwnersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private OwnerService ownerService;

    @MockBean
    private OwnerRepository ownerRepository;

    private Owner generateOwner() {
        Owner owner = new Owner();
        owner.setId(1L);
        owner.setName("name");
        owner.setTelegramId(45843);
        return owner;
    }

    private OwnerDTO getOwnerDTO() {
        OwnerDTO ownerDTO = new OwnerDTO();
        ownerDTO.setName("name");
        ownerDTO.setTelegramId(45843);
        return ownerDTO;
    }

    private JSONObject getOwnerDTOJson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "name");
        jsonObject.put("telegramId", 45843);
        return jsonObject;
    }

    @Test
    public void testAddOwner() throws Exception {
        OwnerDTO ownerDTO = getOwnerDTO();
        JSONObject jsonObject = getOwnerDTOJson();
        Owner owner = generateOwner();

        when(ownerRepository.save(any(Owner.class))).thenReturn(owner);

        mockMvc.perform(post("/bot/owners")
                        .content(jsonObject.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(ownerDTO.getName()));
    }

    @Test
    public void testGetOwners() throws Exception {
        Owner owner = generateOwner();

        when(ownerRepository.findAll()).thenReturn(List.of(owner));
        when(ownerService.getAllOwners()).thenReturn(List.of(owner));

        mockMvc.perform(get("/bot/owners")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(owner.getName()));
    }
}
