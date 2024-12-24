package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.AdopterDTO;
import com.bot.pets_bot.models.entity.Adopter;
import com.bot.pets_bot.services.AdopterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adopter")
public class AdopterController {
    private final AdopterService adopterService;

    public AdopterController(AdopterService adopterService) {
        this.adopterService = adopterService;
    }

    /**
     * Добавление нового усыновителя.
     *
     * @param adopter объект усыновителя, передаваемый в теле запроса.
     * @return созданный объект усыновителя.
     */
    @PostMapping()
    @Operation(summary = "Добавить нового усыновителя", description = "Добавляет нового усыновителя в систему. Параметры усыновителя включают имя, контактные данные и Telegram ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Усыновитель успешно добавлен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Adopter.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<Adopter> addAdopter(
            @Valid @RequestBody
            @Parameter(description = "Данные усыновителя, включая имя, контактную информацию и Telegram ID. Все поля обязательны.")
            AdopterDTO adopter) {

        return ResponseEntity.ok(adopterService.addAdopter(adopter));
    }

    /**
     * Получение списка всех усыновителей.
     *
     * @return список усыновителей.
     */
    @GetMapping()
    @Operation(summary = "Получить всех усыновителей", description = "Возвращает список всех усыновителей из системы.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список усыновителей успешно возвращен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Adopter.class))),
            @ApiResponse(responseCode = "404", description = "Усыновители не найдены")
    })
    public ResponseEntity<List<Adopter>> getAllAdopters() {
        return ResponseEntity.ok(adopterService.getAllAdopters());
    }
}
