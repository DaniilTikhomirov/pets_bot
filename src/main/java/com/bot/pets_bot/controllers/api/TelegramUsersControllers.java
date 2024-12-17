package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.TelegramUserDTO;
import com.bot.pets_bot.models.entity.TelegramUser;
import com.bot.pets_bot.services.TelegramUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class TelegramUsersControllers {

    private final TelegramUserService telegramUserService;

    public TelegramUsersControllers(TelegramUserService telegramUserService) {
        this.telegramUserService = telegramUserService;
    }

    @GetMapping()
    public ResponseEntity<List<TelegramUser>> getUsers() {
        return ResponseEntity.ok(telegramUserService.getTelegramUsers());
    }

    @PostMapping()
    public ResponseEntity<TelegramUser> addUser(@RequestBody TelegramUserDTO telegramUser) {
        return ResponseEntity.ok(telegramUserService.addTelegramUser(telegramUser));
    }
}
