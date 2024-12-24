package com.bot.pets_bot.telegram_utils;

import com.bot.pets_bot.models.dto.ReportsDTO;
import com.bot.pets_bot.models.entity.Reports;
import com.bot.pets_bot.services.AdopterService;
import com.bot.pets_bot.services.AnimalService;
import com.bot.pets_bot.services.VolunteersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Вспомогательный класс для обработки моделей, предоставляющий методы для извлечения страниц из списка
 * и преобразования объектов {@link ReportsDTO} в сущности {@link Reports}.
 */
@Slf4j
@Component
public class ModelsHelper {

    private final AnimalService animalService;
    private final AdopterService adopterService;
    private final VolunteersService volunteersService;

    /**
     * Конструктор для инициализации зависимостей с сервисами.
     *
     * @param animalService Сервис для работы с животными.
     * @param adopterService Сервис для работы с усыновителями.
     * @param volunteersService Сервис для работы с волонтерами.
     */
    public ModelsHelper(AnimalService animalService, AdopterService adopterService, VolunteersService volunteersService) {
        this.animalService = animalService;
        this.adopterService = adopterService;
        this.volunteersService = volunteersService;
    }

    /**
     * Метод для извлечения страницы данных из списка. Позволяет получить подсписок элементов
     * на основе номера страницы и размера страницы.
     *
     * @param page Номер страницы (начиная с 1).
     * @param size Размер страницы (количество элементов на странице).
     * @param list Список, из которого нужно извлечь элементы.
     * @param <T> Тип элементов в списке.
     * @return Список элементов для указанной страницы.
     * @throws IllegalArgumentException если номер страницы меньше 1.
     */
    public static <T> List<T> getPage(int page, int size, List<T> list) {
        if (page < 1) {
            log.error("Page number is less than 1");
            throw new IllegalArgumentException("page must be greater than 0");
        }

        int i = (page * size) - size;

        if (i > list.size() - 1) {
            return new ArrayList<>();
        }

        int maxPageSize = page * size;

        List<T> newList = new ArrayList<>();

        while (i < maxPageSize && i < list.size()) {
            newList.add(list.get(i));
            i++;
        }

        return newList;
    }

    /**
     * Преобразует объект {@link ReportsDTO} в сущность {@link Reports}.
     * Выполняет преобразование, устанавливая значения из DTO в соответствующие поля сущности.
     *
     * @param dto Объект {@link ReportsDTO}, содержащий данные отчета.
     * @return Сущность {@link Reports}, созданная из данных DTO.
     */
    public Reports convertFromDTOReports(ReportsDTO dto) {
        Reports reports = new Reports();
        reports.setText(dto.getText());
        reports.setAnimal(animalService.getAnimalById(dto.getAnimalId()));
        reports.setAdopter(adopterService.getAdopterById(dto.getAdopterId()));
        reports.setVolunteers(volunteersService.getVolunteersById(dto.getVolunteerId()));

        return reports;
    }
}
