package com.bot.pets_bot.reactions;

import com.bot.pets_bot.mark_ups.MarkUps;
import com.bot.pets_bot.models.entity.AnimalsAdvice;
import com.bot.pets_bot.models.entity.DogHandler;
import com.bot.pets_bot.services.AnimalAdviceService;
import com.bot.pets_bot.services.DogHandlerService;
import com.bot.pets_bot.telegram_utils.MessageProvider;
import com.bot.pets_bot.telegram_utils.ModelsHelper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnimalAdviceReactions {

    private final AnimalAdviceService animalAdviceService;
    private final MessageProvider messageProvider;
    private final DogHandlerService dogHandlerService;

    public AnimalAdviceReactions(AnimalAdviceService animalAdviceService,
                                 MessageProvider messageProvider,
                                 DogHandlerService dogHandlerService) {
        this.animalAdviceService = animalAdviceService;
        this.messageProvider = messageProvider;
        this.dogHandlerService = dogHandlerService;
    }

    /**
     * Реакция на ошибку, если объект AnimalsAdvice равен null.
     * <p>
     * Если объект animalsAdvice не найден, метод изменяет текст сообщения и
     * добавляет кнопку для возврата в меню.
     * </p>
     *
     * @param chatId идентификатор чата
     * @param messageId идентификатор сообщения
     * @param animalsAdvice объект советов для животных
     * @return true, если произошла ошибка, иначе false
     */
    private boolean reactionOnError(long chatId, int messageId, AnimalsAdvice animalsAdvice, String prefix) {
        if (animalsAdvice == null) {
            messageProvider.changeText(chatId, messageId, "Произошла ошибка! Попробуйте позже");
            messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToAnimalsAdviceMenu", prefix));
            return true;
        }

        return false;
    }

    /**
     * Реакция на запрос правил знакомства с животным и списка необходимых документов.
     * <p>
     * Отправляет пользователю информацию о правилах знакомства с животным и
     * список документов, необходимых для получения животного из приюта.
     * </p>
     *
     * @param chatId идентификатор чата
     * @param messageId идентификатор сообщения
     */
    public void reactionsOnRulesAndDocument(long chatId, int messageId, long ANIMALS_ADVICE_ID, String prefix) {
        AnimalsAdvice animalsAdvice = animalAdviceService.getAnimalAdviceById(ANIMALS_ADVICE_ID);
        if (reactionOnError(chatId, messageId, animalsAdvice, prefix)) {
            return;
        }

        String info = String.format("""
                        ✨ <b>Правила знакомства с животным:</b>
                        %s
                        
                        📋 <b>Список необходимых документов для получения животного из приюта:</b>
                        %s""",
                animalsAdvice.getRulesForGettingAnimal().trim(),
                animalsAdvice.getDocumentsForAnimal().trim());

        messageProvider.changeText(chatId, messageId, info);
        messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToAnimalsAdviceMenu", prefix));
    }

    /**
     * Реакция на запрос о рекомендациях по транспортировке и обустройству дома для животного.
     * <p>
     * Отправляет пользователю информацию о рекомендациях по транспортировке
     * животного и по обустройству дома для щенка или взрослого животного.
     * </p>
     *
     * @param chatId идентификатор чата
     * @param messageId идентификатор сообщения
     */
    public void reactionOnAnimalGuide(long chatId, int messageId, long ANIMALS_ADVICE_ID, String prefix) {
        AnimalsAdvice animalsAdvice = animalAdviceService.getAnimalAdviceById(ANIMALS_ADVICE_ID);
        if (reactionOnError(chatId, messageId, animalsAdvice, prefix)) {
            return;
        }

        String animal = "щенка";

        if (prefix.equals("cat")){
            animal = "котенка";
        }

        String info = String.format(
                """
                         🎉 1. Рекомендации по транспортировке вашего пушистого друга:
                        %s
                           \s
                         🐾 2. Как сделать дом идеальным для %s? Вот что нужно учесть:
                        %s
                           \s
                         🏡 3. Рекомендации по обустройству дома для вашего взрослого питомца:
                        %s
                           \s
                         ♿ 4. Советы по обустройству дома для животного с ограниченными возможностями:
                        %s
                        \s""",
                animalsAdvice.getRecommendationForMoveAnimal().trim(),
                animal,
                animalsAdvice.getRecommendationForArrangementForPuppy().trim(),
                animalsAdvice.getRecommendationForArrangementForDog().trim(),
                animalsAdvice.getRecommendationForArrangementForDisability().trim()
        );


        messageProvider.changeText(chatId, messageId, info);
        messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToAnimalsAdviceMenu", prefix));
    }

    /**
     * Реакция на советы по первичному общению с собакой от кинолога.
     * <p>
     * Отправляет пользователю советы по первичному общению с его новой собакой
     * от доверенного кинолога.
     * </p>
     *
     * @param chatId идентификатор чата
     * @param messageId идентификатор сообщения
     */
    public void reactionsOnDogHandleAdvice(long chatId, int messageId, long ANIMALS_ADVICE_ID, String prefix) {
        AnimalsAdvice animalsAdvice = animalAdviceService.getAnimalAdviceById(ANIMALS_ADVICE_ID);
        if (reactionOnError(chatId, messageId, animalsAdvice, prefix)) {
            return;
        }

        String emoji = "🐶";
        String animal = "собакой";

        if (prefix.equals("cat")){
            emoji = "\uD83D\uDC31";
            animal = "кошечкой";
        }

        String info = String.format(
                """
                        %s✨ Советы от кинолога по первичному общению с вашей новой %s:
                        %s
                        """,
                emoji,
                animal,
                animalsAdvice.getDogHandlerAdvice().trim());

        messageProvider.changeText(chatId, messageId, info);
        messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToAnimalsAdviceMenu", prefix));

    }

    /**
     * Отправляет список причин отказа в передаче животного.
     *
     * @param chatId Идентификатор чата, в который будет отправлено сообщение.
     * @param messageId Идентификатор сообщения, которое будет изменено.
     */
    public void reactionReasonsForRefusal(long chatId, int messageId, long ANIMALS_ADVICE_ID, String prefix) {
        AnimalsAdvice animalsAdvice = animalAdviceService.getAnimalAdviceById(ANIMALS_ADVICE_ID);
        if (reactionOnError(chatId, messageId, animalsAdvice, prefix)) {
            return;
        }

        String info = String.format(
                """
                        🚫 <b>Причины отказа в передаче животного:</b>
                        Бот может предоставить вам список возможных причин, по которым вам могут отказать \
                        в забирании животного из приюта. Эти причины могут включать:
                        
                        %s""",
                animalsAdvice.getReasonsForRefusal().trim()
        );

        messageProvider.changeText(chatId, messageId, info);
        messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToAnimalsAdviceMenu", prefix));
    }

    /**
     * Отправляет список кинологов с пагинацией.
     *
     * @param chatId Идентификатор чата, в который будет отправлено сообщение.
     * @param messageId Идентификатор сообщения, которое будет изменено.
     * @param page Номер страницы с кинологами.
     * @param del Указывает, нужно ли удалять старое сообщение перед отправкой нового.
     */
    public void putDogHandler(long chatId, int messageId, int page, long ANIMALS_ADVICE_ID, boolean del, String prefix) {
        AnimalsAdvice animalsAdvice = animalAdviceService.getAnimalAdviceById(ANIMALS_ADVICE_ID);
        if (reactionOnError(chatId, messageId, animalsAdvice, prefix)) {
            return;
        }

        if (page < 1){
            return;
        }

        List<DogHandler> dogHandlers = ModelsHelper.getPage(page, 5, animalsAdvice.getDogHandlers());

        if (dogHandlers.isEmpty()) {
            return;
        }
        if (del) {
            messageProvider.deleteMessage(chatId, messageId);
            messageProvider.putMessageWithMarkUps(chatId, "\uD83D\uDC69\u200D⚕️\uD83D\uDC15" +
                    " Наши доверенные кинологи:", MarkUps.getPageDogHandler(page,
                    dogHandlers, prefix));
            return;
        }
        messageProvider.changeText(chatId, messageId, "\uD83D\uDC69\u200D⚕️\uD83D\uDC15" +
                " Наши доверенные кинологи:");
        messageProvider.changeInline(chatId, messageId, MarkUps.getPageDogHandler(page,
                dogHandlers, prefix));
    }

    /**
     * Отправляет информацию о кинологе по заданному идентификатору.
     *
     * @param chatId Идентификатор чата, в который будет отправлено сообщение.
     * @param messageId Идентификатор сообщения, которое будет изменено.
     * @param dogHandlerId Идентификатор кинолога, информацию о котором нужно вывести.
     */
    public void infoAboutDogHandler(long chatId, int messageId, long dogHandlerId, String prefix) {
        DogHandler dogHandler = dogHandlerService.getDogHandler(dogHandlerId);
        if (dogHandler == null) {
            messageProvider.changeText(chatId, messageId, "Возникла Ошибка");
            messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToDogHandlers 1", prefix));
            return;
        }

        String description = dogHandler.getDescription() != null ? dogHandler.getDescription() : "Не указано";

        String info = String.format(
                """
                        <b>🧑‍⚖️ Имя кинолога:</b> %s %s
                        <b>📞 Контакт:</b> %s
                        <b>📝 Описание:</b> %s""",
                dogHandler.getName().trim(),
                dogHandler.getSecondName().trim(),
                dogHandler.getContact().trim(),
                description.trim()
        );

        String photoUrl = dogHandler.getPhotoUrl();

        if (photoUrl == null) {
            messageProvider.changeText(chatId, messageId, info);
            messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToDogHandlers 1", prefix));
            return;
        }

        messageProvider.deleteMessage(chatId, messageId);
        messageProvider.sendPhoto(chatId, info, photoUrl, MarkUps.backButton("backToDogHandlersDel 1", prefix));


    }

}
