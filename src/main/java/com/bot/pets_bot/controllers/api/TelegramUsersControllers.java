package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.TelegramUserDTO;
import com.bot.pets_bot.models.entity.TelegramUser;
import com.bot.pets_bot.services.TelegramUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
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

    /**
     * Получает список всех пользователей Telegram.
     *
     * @return список пользователей Telegram.
     */
    @GetMapping()
    @Operation(summary = "Получить всех пользователей Telegram", description = "Возвращает список всех пользователей Telegram, зарегистрированных в системе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователи Telegram успешно получены", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = TelegramUser.class))),
            @ApiResponse(responseCode = "404", description = "Пользователи не найдены")
    })
    public ResponseEntity<List<TelegramUser>> getUsers() {
        return ResponseEntity.ok(telegramUserService.getTelegramUsers());
    }

    /**
     * Добавляет нового пользователя Telegram.
     *
     * @param telegramUser объект, содержащий данные нового пользователя Telegram.
     * @return добавленный пользователь Telegram.
     */
    @PostMapping()
    @Operation(summary = "Добавить нового пользователя Telegram", description = "Добавляет нового пользователя Telegram в систему.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь Telegram успешно добавлен", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = TelegramUser.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные при добавлении")
    })
    public ResponseEntity<TelegramUser> addUser(
            @RequestBody
            @Parameter(description = "Данные пользователя Telegram для добавления в систему.")
            @Valid TelegramUserDTO telegramUser) {
        return ResponseEntity.ok(telegramUserService.addTelegramUser(telegramUser));
    }
}
