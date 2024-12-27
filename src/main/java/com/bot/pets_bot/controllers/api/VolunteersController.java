package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.VolunteersDTO;
import com.bot.pets_bot.models.entity.Volunteers;
import com.bot.pets_bot.services.VolunteersService;
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
@RequestMapping("/volunteers")
public class VolunteersController {

    private final VolunteersService volunteersService;

    public VolunteersController(VolunteersService volunteersService) {
        this.volunteersService = volunteersService;
    }

    /**
     * Получает список всех волонтеров.
     *
     * @return список волонтеров.
     */
    @GetMapping()
    @Operation(summary = "Получить всех волонтеров", description = "Возвращает список всех волонтеров, зарегистрированных в системе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Волонтеры успешно получены", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Volunteers.class))),
            @ApiResponse(responseCode = "404", description = "Волонтеры не найдены")
    })
    public ResponseEntity<List<Volunteers>> getVolunteers() {
        return ResponseEntity.ok(volunteersService.getVolunteers());
    }

    /**
     * Добавляет нового волонтера.
     *
     * @param volunteers объект, содержащий данные нового волонтера.
     * @return добавленный волонтер.
     */
    @PostMapping()
    @Operation(summary = "Добавить нового волонтера", description = "Добавляет нового волонтера в систему.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Волонтер успешно добавлен", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Volunteers.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные при добавлении")
    })
    public ResponseEntity<Volunteers> addVolunteer(
            @RequestBody
            @Parameter(description = "Данные волонтера для добавления в систему.")
            @Valid VolunteersDTO volunteers) {
        return ResponseEntity.ok(volunteersService.addVolunteers(volunteers));
    }
}
