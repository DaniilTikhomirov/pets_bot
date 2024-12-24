package com.bot.pets_bot.anotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для валидации контактной информации.
 * Используется для проверки формата контакта (например, номера телефона),
 * с помощью кастомного валидатора {@link ContactValidator}.
 * Формат контакта, как правило, должен быть в виде: +d-ddd-ddd-dd-dd.
 */
@Constraint(validatedBy = ContactValidator.class) // Указывает на валидатор для проверки
@Target({ElementType.FIELD}) // Применяется к полям
@Retention(RetentionPolicy.RUNTIME) // Аннотация доступна во время выполнения
public @interface ValidContact {

    /**
     * Сообщение, которое будет возвращено при ошибке валидации.
     * По умолчанию содержит информацию о некорректном формате контакта.
     *
     * @return Сообщение об ошибке валидации
     */
    String message() default "Invalid contact format. Valid format is +d-ddd-ddd-dd-dd";

    /**
     * Группы валидации.
     * Позволяет группировать различные проверки для последующего использования.
     *
     * @return Группы валидации
     */
    Class<?>[] groups() default {};

    /**
     * Дополнительные метаданные для валидации.
     * Могут быть использованы для хранения метаданных валидации, которые
     * не обязательны для работы аннотации.
     *
     * @return Дополнительные метаданные
     */
    Class<? extends Payload>[] payload() default {};
}
