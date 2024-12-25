package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.AnimalDTO;
import com.bot.pets_bot.models.entity.Animal;
import com.bot.pets_bot.services.AnimalService;
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
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    /**
     * Добавляет новое животное в систему.
     *
     * @param animal DTO объект, содержащий информацию о животном.
     * @return добавленное животное.
     */
    @PostMapping()
    @Operation(summary = "Добавить новое животное", description = "Добавляет новое животное в систему. Параметры включают описание, возраст и тип животного.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Животное успешно добавлено",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Animal.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<Animal> addAnimal(
            @RequestBody
            @Parameter(description = "Данные о животном (описание, возраст, тип) для добавления.")
            AnimalDTO animal) {
        return ResponseEntity.ok(animalService.addAnimal(animal));
    }

    /**
     * Добавляет фотографию животного по ID.
     *
     * @param id идентификатор животного, для которого добавляется фотография.
     * @param file изображение животного в формате multipart.
     * @return животное с обновленной фотографией.
     */
    @PostMapping(value = "add_photo/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Добавить фотографию для животного", description = "Добавляет фотографию животного по ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Фотография успешно добавлена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Animal.class))),
            @ApiResponse(responseCode = "400", description = "Некорректный файл")
    })
    public ResponseEntity<Animal> addAnimalPhoto(
            @PathVariable
            @Parameter(description = "ID животного, которому будет добавлена фотография.")
            Long id,
            @RequestBody
            @Parameter(description = "Файл изображения животного.")
            MultipartFile file) {
        return ResponseEntity.ok(animalService.addPhoto(file, id));
    }

    /**
     * Получает страницу с животными.
     *
     * @param page номер страницы.
     * @param size количество животных на странице.
     * @return список животных для указанной страницы.
     */
    @GetMapping("/get_page")
    @Operation(summary = "Получить страницу животных", description = "Получить список животных с пагинацией.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Страница животных успешно получена",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Animal.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры")
    })
    public ResponseEntity<List<Animal>> getAnimalPage(
            @RequestParam("page")
            @Parameter(description = "Номер страницы для получения животных.")
            int page,
            @RequestParam("size")
            @Parameter(description = "Размер страницы (количество животных на странице).")
            int size,
            @RequestParam("cat")
            @Parameter(description = "искать котов ? если нет то собак")
            boolean cat) {
        return ResponseEntity.ok(animalService.getAnimalsPage(page, size, cat));
    }

    /**
     * Получает всех животных.
     *
     * @return список всех животных.
     */
    @GetMapping()
    @Operation(summary = "Получить всех животных", description = "Получить список всех животных без пагинации.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список всех животных успешно получен",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Animal.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры")
    })
    public ResponseEntity<List<Animal>> getAllAnimals() {
        return ResponseEntity.ok(animalService.getAllAnimals());
    }
}
