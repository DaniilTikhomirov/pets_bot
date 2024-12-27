package com.bot.pets_bot.mark_ups;

import com.bot.pets_bot.models.entity.Animal;
import com.bot.pets_bot.models.entity.DogHandler;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для создания и настройки различных клавиатур (кнопок) для бота.
 * Используется для формирования меню с кнопками, которые позволяют пользователю взаимодействовать с ботом.
 */
public class MarkUps {


    /**
     *Создание инлайн-кнопки
     * @param text текст кнопки
     * @param callBack калбэк
     * @return кнопка с указаным текстом и калбэк
     */
    private static InlineKeyboardButton createButton(String text, String callBack, String prefix) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(prefix + "|" + callBack);

        return button;
    }

    public static InlineKeyboardMarkup catOrDog(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton cat = createButton("Кошачий приют", "cat", "cat");
        row1.add(cat);

        InlineKeyboardButton dog = createButton("Собачий приют", "dog", "dog");
        row2.add(dog);

        rows.add(row1);
        rows.add(row2);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }


    /**
     * Создает главное меню с кнопками для взаимодействия с пользователем.
     *
     * @return Инлайн клавиатура с кнопками главного меню.
     */
    public static InlineKeyboardMarkup startMenu(String prefix) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        List<InlineKeyboardButton> row5 = new ArrayList<>();
        List<InlineKeyboardButton> row6 = new ArrayList<>();

        InlineKeyboardButton infoAboutShelter = createButton("Узнать информацию о приюте", "infoAboutShelter", prefix);
        row1.add(infoAboutShelter);

        InlineKeyboardButton getAnimals = createButton("Посмотреть животных", "getAnimals 1", prefix);
        row2.add(getAnimals);

        InlineKeyboardButton infoAboutTakeAnimals = createButton("Как взять животное из приюта", "infoAboutTakeAnimals", prefix);
        row3.add(infoAboutTakeAnimals);

        InlineKeyboardButton sendReportAboutAnimal = createButton("Прислать отчет о питомце", "sendReportAboutAnimal", prefix);
        row4.add(sendReportAboutAnimal);

        InlineKeyboardButton callVolunteer = createButton("Позвать волонтера", "callVolunteer backToMenuDel", prefix);
        row5.add(callVolunteer);

        InlineKeyboardButton back = createButton("Назад", "backToStart", prefix);
        row6.add(back);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        rows.add(row5);
        rows.add(row6);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }


    /**
     * Создает клавиатуру с кнопкой "Назад" для возвращения в предыдущее меню.
     *
     * @param backCallBack Данные для обратного вызова кнопки.
     * @return Инлайн клавиатура с кнопкой "Назад".
     */
    public static InlineKeyboardMarkup backButton(String backCallBack, String prefix) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton back = createButton("Назад", backCallBack, prefix);
        row1.add(back);
        rows.add(row1);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    /**
     * Создает клавиатуру с кнопками для отображения информации о приюте.
     *
     * @return Инлайн клавиатура с кнопками для информации о приюте.
     */
    public static InlineKeyboardMarkup InfoAboutShelterMenu(String prefix) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        List<InlineKeyboardButton> row5 = new ArrayList<>();

        InlineKeyboardButton generalInformation = createButton("Общая информация", "generalInformation", prefix);
        row1.add(generalInformation);

        InlineKeyboardButton securityPrecautions = createButton("Рекомендации о техник безопасности",
                "securityPrecautions", prefix);
        row2.add(securityPrecautions);

        InlineKeyboardButton getContact = createButton("Отправить контакты", "getContact", prefix);
        row3.add(getContact);

        InlineKeyboardButton callVolunteer = createButton("Позвать волонтера",
                "callVolunteer backToInfoAboutShelterMenuDel", prefix);
        row4.add(callVolunteer);

        InlineKeyboardButton back = createButton("Назад", "backToMenu", prefix);

        row5.add(back);
        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        rows.add(row5);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }


    /**
     * Создает клавиатуру с кнопками для отображения информации о приюте.
     *
     * @return Инлайн клавиатура с кнопками для информации о приюте.
     */
    public static ReplyKeyboardMarkup getContact() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        KeyboardButton button = new KeyboardButton("Отправить номер");
        button.setRequestContact(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(button);
        keyboardRows.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

    /**
     * Создает клавиатуру для отображения страницы с животными с возможностью перехода по страницам.
     *
     * @param page    Текущая страница.
     * @param animals Список животных, которые будут отображаться на странице.
     * @return Инлайн клавиатура с кнопками для отображения животных и перехода между страницами.
     */
    public static InlineKeyboardMarkup getPageAnimal(int page, List<Animal> animals, String prefix) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();
        for (Animal animal : animals) {
            String name = animal.getName();
            if (name == null) {
                name = "Имя не указано";
            }
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton button = createButton(name, "click_on_animal " + animal.getId(), prefix);

            rowInline.add(button);
            rowsInlineKeyboardButtons.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton next = createButton("->", "next_animal " + (page + 1), prefix);


        InlineKeyboardButton counter = createButton(page + "", "counter", prefix);

        InlineKeyboardButton prev = createButton("<-", "prev_animal " + (page - 1), prefix);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        InlineKeyboardButton back = createButton("Назад", "backToMenu", prefix);

        rowInline.add(prev);
        rowInline.add(counter);
        rowInline.add(next);
        rowInline2.add(back);

        rowsInlineKeyboardButtons.add(rowInline);
        rowsInlineKeyboardButtons.add(rowInline2);

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;
    }

    /**
     * Создает клавиатуру с кнопками для получения информации о правилах для животных.
     *
     * @return Инлайн клавиатура с кнопками для получения правил для животных.
     */
    public static InlineKeyboardMarkup getRuleForAnimals(String prefix) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        List<InlineKeyboardButton> row5 = new ArrayList<>();
        List<InlineKeyboardButton> row6 = new ArrayList<>();
        List<InlineKeyboardButton> row7 = new ArrayList<>();

        InlineKeyboardButton rulesAndDocument = createButton("Ваш новый друг: правила и документы для встречи",
                "rulesAndDocument", prefix);
        row1.add(rulesAndDocument);

        InlineKeyboardButton animalGuide = createButton("Руководство по уходу за питомцем: от транспортировки до обустройства дома",
                "animalGuide", prefix);
        row2.add(animalGuide);

        InlineKeyboardButton dogHandleAdvice = createButton("Советы по первичному обращению",
                "dogHandleAdvice", prefix);
        row3.add(dogHandleAdvice);

        InlineKeyboardButton dogHandles = createButton("Список кинологов", "dogHandles 1", prefix);
        row4.add(dogHandles);

        InlineKeyboardButton reasonsForRefusal = createButton("Возможные причины отказа",
                "reasonsForRefusal", prefix);
        row5.add(reasonsForRefusal);

        InlineKeyboardButton callVolunteer = createButton("Позвать волонтера",
                "callVolunteer backToAnimalsAdviceMenuDel", prefix);
        row6.add(callVolunteer);

        InlineKeyboardButton getContact = createButton("Отправить контакты", "getContact", prefix);
        row6.add(getContact);

        InlineKeyboardButton back = createButton("Назад", "backToMenu", prefix);
        row7.add(back);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        rows.add(row5);
        rows.add(row6);
        rows.add(row7);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }

    /**
     * Создает клавиатуру для отображения списка кинологов с возможностью перехода по страницам.
     *
     * @param page        Текущая страница.
     * @param dogHandlers Список кинологов для отображения.
     * @return Инлайн клавиатура с кнопками для отображения кинологов и перехода между страницами.
     */
    public static InlineKeyboardMarkup getPageDogHandler(int page, List<DogHandler> dogHandlers, String prefix) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();
        for (DogHandler dogHandler : dogHandlers) {
            String name = dogHandler.getName();
            if (name == null || name.isEmpty()) {
                name = "Имя не указано";
            }
            String secondName = dogHandler.getSecondName();
            if (secondName == null || secondName.isEmpty()) {
                secondName = "Фамилия не указана";
            }
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton button = createButton(name + " " + secondName,
                    "click_on_dogHandler " + dogHandler.getId(), prefix);
            rowInline.add(button);
            rowsInlineKeyboardButtons.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton next = createButton("->", "next_dogHandler " + (page + 1), prefix);

        InlineKeyboardButton counter = createButton(page + "", "counter", prefix);

        InlineKeyboardButton prev = createButton("<-", "prev_dogHandler" +
                " " + (page - 1), prefix);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        InlineKeyboardButton back = createButton("Назад", "backToAnimalsAdviceMenu", prefix);

        rowInline.add(prev);
        rowInline.add(counter);
        rowInline.add(next);
        rowInline2.add(back);

        rowsInlineKeyboardButtons.add(rowInline);
        rowsInlineKeyboardButtons.add(rowInline2);

        inlineKeyboardMarkup.setKeyboard(rowsInlineKeyboardButtons);

        return inlineKeyboardMarkup;
    }


    /**
     * Создает клавиатуру с кнопками для отправки заявки на питомца и возврата в меню.
     *
     * @param backCallBack Данные для обратного вызова кнопки "Назад".
     * @param animalId     Идентификатор животного.
     * @param chatId       Идентификатор чата пользователя.
     * @return Инлайн клавиатура с кнопками для отправки заявки и возврата в меню.
     */
    public static InlineKeyboardMarkup getAnimal(String backCallBack, long animalId, long chatId, String prefix) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton takeAnimal = createButton("Отправить заявку", "takeAnimal " + animalId + " " + chatId, prefix);
        row1.add(takeAnimal);

        InlineKeyboardButton back = createButton("Назад", backCallBack, prefix);
        row2.add(back);
        rows.add(row1);
        rows.add(row2);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    /**
     * Создает клавиатуру с кнопками для принятия или отклонения заявки на животное.
     *
     * @param adoptChatId Идентификатор чата, который подал заявку.
     * @param animalId    Идентификатор животного.
     * @param name        Имя заявителя.
     * @param contact     Контактная информация заявителя.
     * @param acceptCQ    Данные для обратного вызова принятия заявки.
     * @param rejectCQ    Данные для обратного вызова отклонения заявки.
     * @return Инлайн клавиатура с кнопками для принятия или отклонения заявки.
     */
    public static InlineKeyboardMarkup acceptReject(long adoptChatId, long animalId,
                                                    String name, String contact,
                                                    String acceptCQ, String rejectCQ, String prefix) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton accept = createButton("Принять",
                acceptCQ + " " + adoptChatId + " " + animalId + " " + name + " " + contact, prefix);
        row1.add(accept);

        InlineKeyboardButton reject = createButton("Отклонить", rejectCQ + " " + adoptChatId, prefix);
        row1.add(reject);

        rows.add(row1);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }


    /**
     * Создает клавиатуру с кнопками для добавления дней или принятия заявки на питомца.
     *
     * @param adoptChatId Идентификатор чата заявителя.
     * @param animalId    Идентификатор животного.
     * @return Инлайн клавиатура с кнопками для добавления дней или принятия заявки.
     */
    public static InlineKeyboardMarkup addDaysOrAccept(long adoptChatId, long animalId, String prefix) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton add14Days = createButton("Добавить 14 дней",
                "add14Days " + adoptChatId + " " + animalId, prefix);
        row1.add(add14Days);

        InlineKeyboardButton add30Days = createButton("Добавить 30 дней",
                "add30Days " + adoptChatId + " " + animalId, prefix);
        row1.add(add30Days);

        InlineKeyboardButton accept = createButton("Принять", "acceptP " + adoptChatId + " " + animalId, prefix);
        row2.add(accept);

        rows.add(row1);
        rows.add(row2);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
