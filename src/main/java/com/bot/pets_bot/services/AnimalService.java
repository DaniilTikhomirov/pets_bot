package com.bot.pets_bot.services;

import com.bot.pets_bot.exeptions.BadPage;
import com.bot.pets_bot.exeptions.S3Error;
import com.bot.pets_bot.models.dto.AnimalDTO;
import com.bot.pets_bot.models.entity.Animal;
import com.bot.pets_bot.repositories.AnimalRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * Сервис для работы с сущностями животных (Animal).
 * Предоставляет методы для добавления, получения, обновления животных, а также для работы с фотографиями животных и пагинацией.
 */
@Service
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final S3Service s3Service;

    @Value("${s3.selectel.domain}")
    private String domain;

    /**
     * Конструктор сервиса для внедрения зависимостей.
     *
     * @param animalRepository Репозиторий для работы с сущностями Animal.
     * @param s3Service Сервис для работы с S3 хранилищем.
     */
    public AnimalService(AnimalRepository animalRepository, S3Service s3Service) {
        this.animalRepository = animalRepository;
        this.s3Service = s3Service;
    }

    /**
     * Добавляет нового животного.
     *
     * @param animal DTO объекта животного, которое нужно добавить.
     * @return Сущность Animal, сохраненная в базе данных.
     */
    public Animal addAnimal(AnimalDTO animal) {
        return animalRepository.save(Animal.convertFromDTO(animal));
    }

    /**
     * Добавляет фото животному.
     * Загружает файл в S3 хранилище и сохраняет ссылку на фото в сущности Animal.
     *
     * @param file Файл с изображением животного.
     * @param id ID животного, которому нужно добавить фото.
     * @return Обновленная сущность Animal с ссылкой на фото.
     * @throws S3Error Если произошла ошибка при загрузке файла в S3 хранилище.
     */
    public Animal addPhoto(MultipartFile file, long id) {
        Animal animal = animalRepository.findById(id).orElse(null);
        if (animal == null) {
            return null;
        }

        String endPoint = "animal" + animal.getId();
        if(s3Service.uploadFile(file, endPoint)){
            animal.setPhotoUrl(domain + "/" + endPoint + "." + s3Service.
                    getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        } else {
            throw new S3Error("Error uploading file to s3 Storage");
        }

        return animalRepository.save(animal);
    }

    /**
     * Возвращает список всех животных.
     *
     * @return Список всех животных (List).
     */
    public List<Animal> getAllAnimals() {
        return animalRepository.findAll();
    }

    /**
     * Ищет животное по его ID.
     *
     * @param id ID животного.
     * @return Сущность Animal, если животное найдено, иначе null.
     */
    public Animal getAnimalById(long id) {
        return animalRepository.findById(id).orElse(null);
    }

    /**
     * Обновляет данные существующего животного.
     *
     * @param animal Сущность Animal, которая будет обновлена в базе данных.
     * @return Обновленная сущность Animal.
     */
    public Animal putAnimal(Animal animal) {
        return animalRepository.save(animal);
    }

    /**
     * Возвращает страницу животных, которые еще не были усыновлены и не находятся на принятии.
     *
     * @param page Номер страницы (начинается с 1).
     * @param size Количество животных на одной странице.
     * @return Список животных на запрашиваемой странице.
     * @throws BadPage Если номер страницы меньше 1.
     */
    @Transactional
    public List<Animal> getAnimalsPage(int page, int size) {
        if (page < 1) {
            throw new BadPage("your page number is less than 1");
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return animalRepository.findAllByAdopterIsNullAndTakeIsFalse(pageRequest).getContent();
    }
}
