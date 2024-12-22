package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.ShelterInfoDTO;
import com.bot.pets_bot.models.entity.ShelterInfo;
import com.bot.pets_bot.services.ShelterInfoService;
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
@RequestMapping("/shelters")
public class SheltersInfoController {

    private final ShelterInfoService shelterInfoService;

    public SheltersInfoController(ShelterInfoService shelterInfoService) {
        this.shelterInfoService = shelterInfoService;
    }

    /**
     * Получает список информации о приютах.
     *
     * @return список информации о приютах.
     */
    @GetMapping()
    @Operation(summary = "Получить информацию о приютах", description = "Возвращает список всей информации о приютах, доступной в системе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о приютах успешно получена", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ShelterInfo.class))),
            @ApiResponse(responseCode = "404", description = "Информация о приютах не найдена")
    })
    public ResponseEntity<List<ShelterInfo>> getShelters() {
        return ResponseEntity.ok(shelterInfoService.getSheltersInfo());
    }

    /**
     * Добавляет новый приют.
     *
     * @param shelterInfo объект, содержащий данные о приюте.
     * @return добавленный приют.
     */
    @PostMapping()
    @Operation(summary = "Добавить новый приют", description = "Добавляет информацию о новом приюте в систему.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о приюте успешно добавлена", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ShelterInfo.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные при добавлении")
    })
    public ResponseEntity<ShelterInfo> addShelter(
            @RequestBody
            @Valid
            @Parameter(description = "Данные приюта для добавления в систему.")
            ShelterInfoDTO shelterInfo) {
        return ResponseEntity.ok(shelterInfoService.addShelterInfo(shelterInfo));
    }
}
