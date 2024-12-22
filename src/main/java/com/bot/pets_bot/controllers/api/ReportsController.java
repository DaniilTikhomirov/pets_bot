package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.models.dto.ReportsDTO;
import com.bot.pets_bot.models.entity.Reports;
import com.bot.pets_bot.services.ReportsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportsController {
    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService){
        this.reportsService = reportsService;
    }

    /**
     * Добавляет новый отчет.
     *
     * @param reportsDTO объект, содержащий данные отчета.
     * @return созданный отчет.
     */
    @PostMapping()
    @Operation(summary = "Добавить новый отчет", description = "Добавляет новый отчет в систему.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отчет успешно добавлен", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Reports.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    public ResponseEntity<Reports> addReports(@RequestBody ReportsDTO reportsDTO){
        return ResponseEntity.ok(reportsService.addReports(reportsDTO));
    }

    @GetMapping()
    public ResponseEntity<List<Reports>> getAllReports(){
        return ResponseEntity.ok(reportsService.getAllReports());
    }
}
