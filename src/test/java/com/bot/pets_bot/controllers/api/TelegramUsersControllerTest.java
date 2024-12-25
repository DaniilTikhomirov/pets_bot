package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.TelegramUserDTO;
import com.bot.pets_bot.models.entity.TelegramUser;
import com.bot.pets_bot.services.TelegramUserService;
import com.bot.pets_bot.repositories.TelegramUsersRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TelegramUsersControllers.class)
public class TelegramUsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TelegramUserService telegramUserService;

    @MockBean
    private TelegramUsersRepository telegramUsersRepository;

    @InjectMocks
    private TelegramUsersControllers telegramUsersControllers;

    private TelegramUser generateTelegramUser() {
        TelegramUser user = new TelegramUser();
        user.setId(1L);
        user.setTelegramId(12345L);
        user.setName("Test User");
        user.setContact("+7-777-777-77-77");
        return user;
    }

    private TelegramUserDTO generateTelegramUserDTO() {
        TelegramUserDTO dto = new TelegramUserDTO();
        dto.setTelegramId(12345L);
        dto.setName("Test User");
        dto.setContact("+7-777-777-77-77");
        return dto;
    }

    @Test
    public void testGetUsers() throws Exception {
        TelegramUser telegramUser = generateTelegramUser();
        when(telegramUserService.getTelegramUsers()).thenReturn(List.of(telegramUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/bot/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(telegramUser.getName()))
                .andExpect(jsonPath("$[0].contact").value(telegramUser.getContact()));
    }

    @Test
    public void testAddUser() throws Exception {
        TelegramUser telegramUser = generateTelegramUser();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("telegramId", telegramUser.getTelegramId());
        jsonObject.put("name", telegramUser.getName());
        jsonObject.put("contact", telegramUser.getContact());

        when(telegramUserService.addTelegramUser(any(TelegramUserDTO.class))).thenReturn(telegramUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/bot/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telegramId").value(telegramUser.getTelegramId()))
                .andExpect(jsonPath("$.name").value(telegramUser.getName()));
    }
}
