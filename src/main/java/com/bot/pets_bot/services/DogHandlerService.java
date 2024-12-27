package com.bot.pets_bot.services;

import com.bot.pets_bot.exeptions.BadPage;
import com.bot.pets_bot.exeptions.S3Error;
import com.bot.pets_bot.models.entity.DogHandler;
import com.bot.pets_bot.repositories.DogHandlerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * Сервис для работы с кинологами.
 * Предоставляет методы для добавления, получения, обновления информации о кинологах,
 * а также для работы с фотографиями кинологов и пагинацией.
 */
@Service
public class DogHandlerService {
    private final DogHandlerRepository dogHandlerRepository;
    private final S3Service s3Service;

    @Value("${s3.selectel.domain}")
    private String domain;

    /**
     * Конструктор сервиса для внедрения зависимостей.
     *
     * @param dogHandlerRepository Репозиторий для работы с сущностями DogHandler (кинологи).
     * @param s3Service Сервис для работы с S3 хранилищем.
     */
    public DogHandlerService(DogHandlerRepository dogHandlerRepository, S3Service s3Service) {
        this.dogHandlerRepository = dogHandlerRepository;
        this.s3Service = s3Service;
    }

    /**
     * Ищет кинолога по его ID.
     *
     * @param id ID кинолога.
     * @return Сущность DogHandler (кинолог), если найден, иначе null.
     */

    @Cacheable("dogHandlers")
    public DogHandler getDogHandler(long id) {
        return dogHandlerRepository.findById(id).orElse(null);
    }

    /**
     * Добавляет фото для кинолога.
     * Загружает файл в S3 хранилище и сохраняет ссылку на фото в сущности DogHandler.
     *
     * @param file Файл с изображением кинолога.
     * @param dogId ID кинолога, которому нужно добавить фото.
     * @return Обновленная сущность DogHandler с ссылкой на фото.
     * @throws S3Error Если произошла ошибка при загрузке файла в S3 хранилище.
     */

    @CachePut(value = "dogHandlers", key = "#dogId")
    public DogHandler addPhoto(MultipartFile file, long dogId) {
        DogHandler dogHandler = dogHandlerRepository.findById(dogId).orElse(null);
        if (dogHandler == null) {
            return null;
        }

        String endPoint = "dog_handler" + dogHandler.getId();
        if (s3Service.uploadFile(file, endPoint)) {
            dogHandler.setPhotoUrl(domain + "/" + endPoint + "." + s3Service.
                    getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        } else {
            throw new S3Error("Error uploading file to s3 Storage");
        }

        return dogHandlerRepository.save(dogHandler);
    }

    /**
     * Возвращает страницу с кинологами.
     *
     * @param page Номер страницы (начинается с 1).
     * @param size Количество кинологов на одной странице.
     * @return Список кинологов на запрашиваемой странице.
     * @throws BadPage Если номер страницы меньше 1.
     */
    public List<DogHandler> getDogHandlerPage(int page, int size) {
        if (page < 1) {
            throw new BadPage("your page number is less than 1");
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return dogHandlerRepository.findAll(pageRequest).getContent();
    }
}
