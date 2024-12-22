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
     * Создает главное меню с кнопками для взаимодействия с пользователем.
     *
     * @return Инлайн клавиатура с кнопками главного меню.
     */
    public static InlineKeyboardMarkup startMenu(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        List<InlineKeyboardButton> row5 = new ArrayList<>();

        InlineKeyboardButton infoAboutShelter = new InlineKeyboardButton();
        infoAboutShelter.setText("Узнать информацию о приюте");
        infoAboutShelter.setCallbackData("infoAboutShelter");
        row1.add(infoAboutShelter);

        InlineKeyboardButton getAnimals = new InlineKeyboardButton();
        getAnimals.setText("Посмотреть собак");
        getAnimals.setCallbackData("getAnimals 1");
        row2.add(getAnimals);

        InlineKeyboardButton infoAboutTakeAnimals = new InlineKeyboardButton();
        infoAboutTakeAnimals.setText("Как взять животное из приюта");
        infoAboutTakeAnimals.setCallbackData("infoAboutTakeAnimals");
        row3.add(infoAboutTakeAnimals);

        InlineKeyboardButton sendReportAboutAnimal = new InlineKeyboardButton();
        sendReportAboutAnimal.setText("Прислать отчет о питомце");
        sendReportAboutAnimal.setCallbackData("sendReportAboutAnimal");
        row4.add(sendReportAboutAnimal);

        InlineKeyboardButton callVolunteer = new InlineKeyboardButton();
        callVolunteer.setText("Позвать волонтера");
        callVolunteer.setCallbackData("callVolunteer backToMenuDel");
        row5.add(callVolunteer);

        rows.add(row1);
        rows.add(row2);
        rows.add(row3);
        rows.add(row4);
        rows.add(row5);

        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }


    /**
     * Создает клавиатуру с кнопкой "Назад" для возвращения в предыдущее меню.
     *
     * @param backCallBack Данные для обратного вызова кнопки.
     * @return Инлайн клавиатура с кнопкой "Назад".
     */
    public static InlineKeyboardMarkup backButton(String backCallBack){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");
        back.setCallbackData(backCallBack);
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
    public static InlineKeyboardMarkup InfoAboutShelterMenu(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        List<InlineKeyboardButton> row5 = new ArrayList<>();

        InlineKeyboardButton generalInformation = new InlineKeyboardButton();
        generalInformation.setText("Общая информация");
        generalInformation.setCallbackData("generalInformation");
        row1.add(generalInformation);

        InlineKeyboardButton securityPrecautions = new InlineKeyboardButton();
        securityPrecautions.setText("Рекомендации о техник безопасности");
        securityPrecautions.setCallbackData("securityPrecautions");
        row2.add(securityPrecautions);

        InlineKeyboardButton getContact = new InlineKeyboardButton();
        getContact.setText("Отправить контакты");
        getContact.setCallbackData("getContact");
        row3.add(getContact);

        InlineKeyboardButton callVolunteer = new InlineKeyboardButton();
        callVolunteer.setText("Позвать волонтера");
        callVolunteer.setCallbackData("callVolunteer backToInfoAboutShelterMenuDel");
        row4.add(callVolunteer);

        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");
        back.setCallbackData("backToMenu");
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
    public static ReplyKeyboardMarkup getContact(){
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
     * @param page Текущая страница.
     * @param animals Список животных, которые будут отображаться на странице.
     * @return Инлайн клавиатура с кнопками для отображения животных и перехода между страницами.
     */
    public static InlineKeyboardMarkup getPageAnimal(int page, List<Animal> animals){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();
        for (Animal animal : animals) {
            String name = animal.getName();
            if(name == null){
                name = "Имя не указано";
            }
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(name);

            button.setCallbackData("click_on_animal " + animal.getId());

            rowInline.add(button);
            rowsInlineKeyboardButtons.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton next = new InlineKeyboardButton();

        next.setText("->");
        next.setCallbackData("next_animal " + (page + 1));

        InlineKeyboardButton counter = new InlineKeyboardButton();
        counter.setText(page + "");
        counter.setCallbackData("counter ");

        InlineKeyboardButton prev = new InlineKeyboardButton();
        prev.setText("<-");
        prev.setCallbackData("prev_animal " + (page - 1));

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");
        back.setCallbackData("backToMenu");

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
    public static InlineKeyboardMarkup getRuleForAnimals(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        List<InlineKeyboardButton> row5 = new ArrayList<>();
        List<InlineKeyboardButton> row6 = new ArrayList<>();
        List<InlineKeyboardButton> row7 = new ArrayList<>();

        InlineKeyboardButton rulesAndDocument = new InlineKeyboardButton();
        rulesAndDocument.setText("Ваш новый друг: правила и документы для встречи");
        rulesAndDocument.setCallbackData("rulesAndDocument");
        row1.add(rulesAndDocument);

        InlineKeyboardButton animalGuide = new InlineKeyboardButton();
        animalGuide.setText("Руководство по уходу за питомцем: от транспортировки до обустройства дома");
        animalGuide.setCallbackData("animalGuide");
        row2.add(animalGuide);

        InlineKeyboardButton dogHandleAdvice = new InlineKeyboardButton();
        dogHandleAdvice.setText("Советы по первичному обращению");
        dogHandleAdvice.setCallbackData("dogHandleAdvice");
        row3.add(dogHandleAdvice);

        InlineKeyboardButton dogHandles = new InlineKeyboardButton();
        dogHandles.setText("Список кинологов");
        dogHandles.setCallbackData("dogHandles 1");
        row4.add(dogHandles);

        InlineKeyboardButton reasonsForRefusal = new InlineKeyboardButton();
        reasonsForRefusal.setText("Возможные причины отказа");
        reasonsForRefusal.setCallbackData("reasonsForRefusal");
        row5.add(reasonsForRefusal);

        InlineKeyboardButton callVolunteer = new InlineKeyboardButton();
        callVolunteer.setText("Позвать волонтера");
        callVolunteer.setCallbackData("callVolunteer backToAnimalsAdviceMenuDel");
        row6.add(callVolunteer);

        InlineKeyboardButton getContact = new InlineKeyboardButton();
        getContact.setText("Отправить контакты");
        getContact.setCallbackData("getContact");
        row6.add(getContact);

        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");
        back.setCallbackData("backToMenu");
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
     * @param page Текущая страница.
     * @param dogHandlers Список кинологов для отображения.
     * @return Инлайн клавиатура с кнопками для отображения кинологов и перехода между страницами.
     */
    public static InlineKeyboardMarkup getPageDogHandler(int page, List<DogHandler> dogHandlers){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInlineKeyboardButtons = new ArrayList<>();
        for (DogHandler dogHandler : dogHandlers) {
            String name = dogHandler.getName();
            if(name == null || name.isEmpty()){
                name = "Имя не указано";
            }
            String secondName = dogHandler.getSecondName();
            if(secondName == null || secondName.isEmpty()){
                secondName = "Фамилия не указана";
            }
            List<InlineKeyboardButton> rowInline = new ArrayList<>();

            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(name + " " + secondName);

            button.setCallbackData("click_on_dogHandler " + dogHandler.getId());

            rowInline.add(button);
            rowsInlineKeyboardButtons.add(rowInline);

        }

        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton next = new InlineKeyboardButton();

        next.setText("->");
        next.setCallbackData("next_dogHandler " + (page + 1));

        InlineKeyboardButton counter = new InlineKeyboardButton();
        counter.setText(page + "");
        counter.setCallbackData("counter ");

        InlineKeyboardButton prev = new InlineKeyboardButton();
        prev.setText("<-");
        prev.setCallbackData("prev_dogHandler " + (page - 1));

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();

        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");
        back.setCallbackData("backToAnimalsAdviceMenu");

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
     * @param animalId Идентификатор животного.
     * @param chatId Идентификатор чата пользователя.
     * @return Инлайн клавиатура с кнопками для отправки заявки и возврата в меню.
     */
    public static InlineKeyboardMarkup getAnimal(String backCallBack, long animalId, long chatId){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton takeAnimal = new InlineKeyboardButton();
        takeAnimal.setText("Отправить заявку");
        takeAnimal.setCallbackData("takeAnimal " + animalId + " " + chatId);
        row1.add(takeAnimal);

        InlineKeyboardButton back = new InlineKeyboardButton();
        back.setText("Назад");
        back.setCallbackData(backCallBack);
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
     * @param animalId Идентификатор животного.
     * @param name Имя заявителя.
     * @param contact Контактная информация заявителя.
     * @param acceptCQ Данные для обратного вызова принятия заявки.
     * @param rejectCQ Данные для обратного вызова отклонения заявки.
     * @return Инлайн клавиатура с кнопками для принятия или отклонения заявки.
     */
    public static InlineKeyboardMarkup acceptReject(long adoptChatId, long animalId,
                                                    String name, String contact,
                                                    String acceptCQ, String rejectCQ){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton accept = new InlineKeyboardButton();
        accept.setText("Принять");
        accept.setCallbackData(acceptCQ + " " + adoptChatId + " " + animalId + " " + name + " " + contact);
        row1.add(accept);

        InlineKeyboardButton reject = new InlineKeyboardButton();
        reject.setText("Отклонить");
        reject.setCallbackData(rejectCQ + " " + adoptChatId);
        row1.add(reject);

        rows.add(row1);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }


    /**
     * Создает клавиатуру с кнопками для добавления дней или принятия заявки на питомца.
     *
     * @param adoptChatId Идентификатор чата заявителя.
     * @param animalId Идентификатор животного.
     * @return Инлайн клавиатура с кнопками для добавления дней или принятия заявки.
     */
    public static InlineKeyboardMarkup addDaysOrAccept(long adoptChatId, long animalId){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        InlineKeyboardButton add14Days = new InlineKeyboardButton();
        add14Days.setText("Добавить 14 дней");
        add14Days.setCallbackData("add14Days " + adoptChatId + " " + animalId);
        row1.add(add14Days);

        InlineKeyboardButton add30Days = new InlineKeyboardButton();
        add30Days.setText("Добавить 30 дней");
        add30Days.setCallbackData("add30Days " + adoptChatId + " " + animalId);
        row1.add(add30Days);

        InlineKeyboardButton accept = new InlineKeyboardButton();
        accept.setText("Принять");
        accept.setCallbackData("acceptP " + adoptChatId + " " + animalId);
        row2.add(accept);

        rows.add(row1);
        rows.add(row2);

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }



}
