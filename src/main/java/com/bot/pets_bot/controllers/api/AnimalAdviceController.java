package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.AnimalsAdviceDTO;
import com.bot.pets_bot.models.entity.AnimalsAdvice;
import com.bot.pets_bot.services.AnimalAdviceService;
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

/**
 * Контроллер для управления рекомендациями по уходу за животными.
 * Включает методы для получения всех рекомендаций, добавления новых и обновления существующих рекомендаций.
 */
@RestController
@RequestMapping("/animal_advice")
public class AnimalAdviceController {
    private final AnimalAdviceService animalAdviceService;

    public AnimalAdviceController(AnimalAdviceService animalAdviceService) {
        this.animalAdviceService = animalAdviceService;
    }

    /**
     * Получить все рекомендации по уходу за животными.
     *
     * @return Список всех рекомендаций по уходу за животными.
     */
    @GetMapping()
    @Operation(summary = "Получить все рекомендации по уходу за животными",
            description = "Этот метод возвращает список всех рекомендаций по уходу за животными в системе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Рекомендации успешно получены",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnimalsAdvice.class))),
            @ApiResponse(responseCode = "404", description = "Рекомендации не найдены")
    })
    public ResponseEntity<List<AnimalsAdvice>> getAnimalsAdvices() {
        return ResponseEntity.ok(animalAdviceService.getAllAnimalAdvice());
    }

    /**
     * Добавить новые рекомендации по уходу за животными.
     *
     * @param animalsAdvice DTO с данными для добавления новых рекомендаций.
     * @return Созданные рекомендации.
     */
    @PostMapping()
    @Operation(summary = "Добавить новые рекомендации по уходу за животными",
            description = "Этот метод добавляет новые рекомендации по уходу за животными в систему.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Рекомендации успешно добавлены",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnimalsAdvice.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<AnimalsAdvice> addAnimalAdvice(
            @Valid @RequestBody
            @Parameter(description = "DTO с данными рекомендаций по уходу за животными. Все поля обязательны.")
            AnimalsAdviceDTO animalsAdvice) {

        return ResponseEntity.ok(animalAdviceService.addAnimalAdvice(animalsAdvice));
    }

    /**
     * Обновить существующие рекомендации по уходу за животными.
     *
     * @param animalsAdvice Сущность с данными для обновления.
     * @return Обновленные рекомендации.
     */
    @PutMapping()
    @Operation(summary = "Обновить рекомендации по уходу за животными",
            description = "Этот метод обновляет существующие рекомендации по уходу за животными в системе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Рекомендации успешно обновлены",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AnimalsAdvice.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Рекомендации не найдены")
    })
    public ResponseEntity<AnimalsAdvice> putAnimalAdvice(
            @Valid @RequestBody
            @Parameter(description = "Сущность с данными для обновления рекомендаций по уходу за животными. Все поля обязательны.")
            AnimalsAdvice animalsAdvice) {

        return ResponseEntity.ok(animalAdviceService.putAnimalAdvice(animalsAdvice));
    }
}
