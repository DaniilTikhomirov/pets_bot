package com.bot.pets_bot.controllers.api;

import com.bot.pets_bot.services.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    private final S3Service s3Service;

    public FileUploadController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    /**
     * Загружает файл на S3.
     *
     * @param fileName имя файла, под которым он будет сохранен на S3.
     * @param file файл, который необходимо загрузить.
     * @return true, если файл успешно загружен, иначе false.
     */
    @PostMapping(value = "/upload{fileName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить файл на S3", description = "Загружает файл на S3 с указанным именем.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Файл успешно загружен", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Ошибка при загрузке файла")
    })
    public boolean uploadFile(
            @PathVariable
            @Parameter(description = "Имя файла, под которым он будет сохранен на S3.")
            String fileName,
            @RequestParam("file")
            @Parameter(description = "Файл, который необходимо загрузить.")
            MultipartFile file) {
        return s3Service.uploadFile(file, fileName);
    }
}
