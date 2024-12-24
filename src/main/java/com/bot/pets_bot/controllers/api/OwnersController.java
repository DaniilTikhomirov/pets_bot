package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.OwnerDTO;
import com.bot.pets_bot.models.entity.Owner;
import com.bot.pets_bot.services.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
public class OwnersController {

    private final OwnerService ownerService;

    public OwnersController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    /**
     * Добавляет нового владельца.
     *
     * @param ownerDTO объект, содержащий данные владельца.
     * @return созданный объект владельца.
     */
    @PostMapping()
    @Operation(summary = "Добавить нового владельца", description = "Добавляет нового владельца в систему.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Владелец успешно добавлен", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Owner.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<Owner> addOwner(
            @RequestBody
            @Parameter(description = "Данные владельца для добавления в систему.")
            OwnerDTO ownerDTO) {
        return ResponseEntity.ok(ownerService.addOwner(ownerDTO));
    }

    /**
     * Получает список всех владельцев.
     *
     * @return список владельцев.
     */
    @GetMapping()
    @Operation(summary = "Получить всех владельцев", description = "Возвращает список всех владельцев в системе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список владельцев успешно получен", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Owner.class))),
            @ApiResponse(responseCode = "404", description = "Владельцы не найдены")
    })
    public ResponseEntity<List<Owner>> getOwners() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }
}
