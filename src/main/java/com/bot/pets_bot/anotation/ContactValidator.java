package com.bot.pets_bot.anotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Валидатор для проверки формата контактной информации (например, телефонного номера).
 * Реализует {@link ConstraintValidator}, чтобы проверить строку на соответствие заданному формату.
 * Формат контакта должен быть: +X-XXX-XXX-XX-XX, где X — это цифра.
 */
public class ContactValidator implements ConstraintValidator<ValidContact, String> {

    // Регулярное выражение для проверки формата номера телефона.
    private static final String CONTACT_REGEX = "^\\+\\d+-\\d{3}-\\d{3}-\\d{2}-\\d{2}$";

    /**
     * Метод для валидации строки, которая должна быть номером телефона в определенном формате.
     *
     * @param value Значение для проверки. Это строка, которая должна соответствовать формату контакта.
     * @param constraintValidatorContext Контекст валидации.
     * @return true, если строка соответствует формату, иначе false.
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        // Если значение null, возвращаем false.
        if (value == null) {
            return false;
        }
        // Проверка соответствия значения регулярному выражению.
        return value.matches(CONTACT_REGEX);
    }
}
