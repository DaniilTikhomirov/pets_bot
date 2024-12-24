package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.entity.DogHandler;
import com.bot.pets_bot.services.DogHandlerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/dog_handler")
public class DogHandlerController {

    private final DogHandlerService dogHandlerService;

    public DogHandlerController(DogHandlerService dogHandlerService) {
        this.dogHandlerService = dogHandlerService;
    }

    /**
     * Получает кинолога по его ID.
     *
     * @param id идентификатор кинолога.
     * @return кинолог с указанным ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить кинолога по ID", description = "Получает кинолога по указанному ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Кинолог найден",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DogHandler.class))),
            @ApiResponse(responseCode = "404", description = "Кинолог не найден")
    })
    public ResponseEntity<DogHandler> getDogHandlerById(
            @PathVariable
            @Parameter(description = "ID кинолога для получения данных о нем.")
            long id) {
        return ResponseEntity.ok(dogHandlerService.getDogHandler(id));
    }

    /**
     * Получает страницу кинологов с пагинацией.
     *
     * @param page номер страницы.
     * @param size количество кинологов на странице.
     * @return список кинологов для указанной страницы.
     */
    @GetMapping("/get_page")
    @Operation(summary = "Получить страницу кинологов", description = "Получить список кинологов с пагинацией.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Страница кинологов успешно получена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DogHandler.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры")
    })
    public ResponseEntity<List<DogHandler>> getDogHandlerByPage(
            @RequestParam("page")
            @Parameter(description = "Номер страницы для получения кинологов.")
            int page,
            @RequestParam("size")
            @Parameter(description = "Размер страницы (количество кинологов на странице).")
            int size) {
        return ResponseEntity.ok(dogHandlerService.getDogHandlerPage(page, size));
    }

    /**
     * Добавляет фотографию для кинолога.
     *
     * @param id идентификатор кинолога, для которого добавляется фотография.
     * @param file файл изображения для кинолога.
     * @return кинолог с обновленной фотографией.
     */
    @PostMapping(value = "add_photo/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Добавить фотографию для кинолога", description = "Добавляет фотографию для кинолога по его ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Фотография успешно добавлена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DogHandler.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный файл или ID кинолога")
    })
    public ResponseEntity<DogHandler> addPhoto(
            @PathVariable
            @Parameter(description = "ID кинолога для добавления фотографии.")
            long id,
            @RequestParam("file")
            @Parameter(description = "Файл фотографии для кинолога.")
            MultipartFile file) {
        return ResponseEntity.ok(dogHandlerService.addPhoto(file, id));
    }
}
