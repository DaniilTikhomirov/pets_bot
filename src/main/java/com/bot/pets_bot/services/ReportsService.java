package com.bot.pets_bot.services;

import com.bot.pets_bot.exeptions.S3Error;
import com.bot.pets_bot.models.dto.ReportsDTO;
import com.bot.pets_bot.models.entity.Reports;
import com.bot.pets_bot.repositories.ReportsRepository;
import com.bot.pets_bot.telegram_utils.ModelsHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Сервис для работы с отчетами.
 * Предоставляет методы для добавления отчетов, получения всех отчетов,
 * а также для работы с фотографиями отчетов (добавление и сохранение).
 */
@Service
public class ReportsService {
    private final ReportsRepository reportsRepository;
    private final ModelsHelper modelsHelper;
    private final S3Service s3Service;

    @Value("${s3.selectel.domain}")
    private String domain;

    /**
     * Конструктор сервиса для внедрения зависимостей репозитория отчетов,
     * помощника моделей и сервиса для работы с S3.
     *
     * @param reportsRepository Репозиторий для работы с отчетами.
     * @param modelsHelper Помощник для преобразования DTO в сущности.
     * @param s3Service Сервис для работы с файлами на S3.
     */
    public ReportsService(ReportsRepository reportsRepository, ModelsHelper modelsHelper, S3Service s3Service){
        this.reportsRepository = reportsRepository;
        this.modelsHelper = modelsHelper;
        this.s3Service = s3Service;
    }

    /**
     * Добавляет новый отчет в систему.
     *
     * @param reports DTO объект отчета.
     * @return Сохраненный отчет.
     */
    public Reports addReports(ReportsDTO reports){
        return reportsRepository.save(modelsHelper.convertFromDTOReports(reports));
    }

    /**
     * Обновляет существующий отчет.
     *
     * @param reports Сущность отчета.
     * @return Обновленный отчет.
     */
    public Reports putReports(Reports reports){
        return reportsRepository.save(reports);
    }

    /**
     * Возвращает список всех отчетов.
     *
     * @return Список отчетов.
     */
    public List<Reports> getAllReports(){
        return reportsRepository.findAll();
    }

    /**
     * Получает отчет по идентификатору.
     *
     * @param id Идентификатор отчета.
     * @return Отчет с указанным идентификатором, или null, если отчет не найден.
     */
    @Transactional
    public Reports getReportsById(long id){
        return reportsRepository.findById(id).orElse(null);
    }

    /**
     * Добавляет фотографию к отчету с использованием MultipartFile.
     * Загружает файл на S3 и сохраняет ссылку на фото в объект отчета.
     *
     * @param file Файл изображения.
     * @param reportsId Идентификатор отчета.
     * @return Обновленный отчет с добавленной фотографией.
     * @throws S3Error Исключение, если произошла ошибка загрузки на S3.
     */
    @Transactional
    public Reports addPhoto(MultipartFile file, long reportsId) {
        Reports reports = getReportsById(reportsId);
        if (reports == null) {
            return null;
        }

        String endPoint = "reports" + reports.getId();
        if(s3Service.uploadFile(file, endPoint)){
            reports.setPhotoUrl(domain + "/" + endPoint + "." + s3Service.
                    getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        }else{
            throw new S3Error("Error uploading file to s3 Storage");
        }

        return reportsRepository.save(reports);
    }

    /**
     * Добавляет фотографию к отчету с использованием файла на диске.
     * Загружает файл на S3 и сохраняет ссылку на фото в объект отчета.
     *
     * @param file Файл изображения.
     * @param reportsId Идентификатор отчета.
     * @return Обновленный отчет с добавленной фотографией.
     * @throws S3Error Исключение, если произошла ошибка загрузки на S3.
     */
    public Reports addPhoto(File file, long reportsId) {
        Reports reports = getReportsById(reportsId);
        if (reports == null) {
            return null;
        }

        String endPoint = "reports" + reports.getId();
        if(s3Service.uploadFile(file, endPoint)){
            reports.setPhotoUrl(domain + "/" + endPoint + "." + s3Service.
                    getExtension(Objects.requireNonNull(file.getName())));
        }else{
            throw new S3Error("Error uploading file to s3 Storage");
        }

        return reportsRepository.save(reports);
    }
}
