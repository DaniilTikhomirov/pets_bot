package com.bot.pets_bot.reactions;

import com.bot.pets_bot.mark_ups.MarkUps;
import com.bot.pets_bot.models.entity.Adopter;
import com.bot.pets_bot.models.entity.Animal;
import com.bot.pets_bot.models.entity.TelegramUser;
import com.bot.pets_bot.models.entity.Volunteers;
import com.bot.pets_bot.services.AdopterService;
import com.bot.pets_bot.services.AnimalService;
import com.bot.pets_bot.services.TelegramUserService;
import com.bot.pets_bot.services.VolunteersService;
import com.bot.pets_bot.telegram_utils.MessageProvider;
import com.bot.pets_bot.telegram_utils.StatesStorage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class AnimalsReactions {

    private final AnimalService animalService;
    private final MessageProvider messageProvider;
    private final TelegramUserService telegramUserService;
    private final VolunteersService volunteersService;
    private final AdopterService adopterService;
    private final StatesStorage statesStorage;

    public AnimalsReactions(AnimalService animalService, MessageProvider messageProvider, TelegramUserService telegramUserService, VolunteersService volunteersService, AdopterService adopterService, StatesStorage statesStorage) {
        this.animalService = animalService;
        this.messageProvider = messageProvider;
        this.telegramUserService = telegramUserService;
        this.volunteersService = volunteersService;
        this.adopterService = adopterService;
        this.statesStorage = statesStorage;
    }

    /**
     * Отправляет список животных для отображения на странице с пагинацией.
     *
     * @param chatId Идентификатор чата, в который будет отправлено сообщение.
     * @param messageId Идентификатор сообщения, которое будет изменено.
     * @param page Номер страницы с животными.
     * @param del Указывает, нужно ли удалять старое сообщение перед отправкой нового.
     */
    public void putAnimals(long chatId, int messageId, int page, boolean del) {
        if (page < 1){
            return;
        }
        List<Animal> animals = animalService.getAnimalsPage(page, 5);
        if (animals.isEmpty()) {
            return;
        }
        if (del) {
            messageProvider.deleteMessage(chatId, messageId);
            messageProvider.putMessageWithMarkUps(chatId, "🐾 <b>Добро пожаловать в меню приюта для собак!</b> 🐶\n" +
                    "Выберите собаку из списка ниже, чтобы узнать о ней больше:", MarkUps.getPageAnimal(page,
                    animals));
            return;
        }
        messageProvider.changeText(chatId, messageId, "🐾 <b>Добро пожаловать в меню приюта для собак!</b> 🐶\n" +
                "Выберите собаку из списка ниже, чтобы узнать о ней больше:");
        messageProvider.changeInline(chatId, messageId, MarkUps.getPageAnimal(page,
                animals));
    }

    /**
     * Отправляет информацию о животном по заданному идентификатору.
     *
     * @param chatId Идентификатор чата, в который будет отправлено сообщение.
     * @param messageId Идентификатор сообщения, которое будет изменено.
     * @param animalId Идентификатор животного, информацию о котором нужно вывести.
     */
    public void infoAboutAnimal(long chatId, int messageId, long animalId) {
        Animal animal = animalService.getAnimalById(animalId);
        if (animal == null) {
            messageProvider.changeText(chatId, messageId, "Возникла Ошибка");
            messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToAnimals 1"));
            return;
        }

        String status = animal.getStatus() != null && animal.getStatus().getStatusName() != null
                ? animal.getStatus().getStatusName()
                : "не указан";

        String name = animal.getName() != null
                ? animal.getName()
                : "не указано";

        String description = animal.getDescription() != null
                ? animal.getDescription()
                : "нет описания";

        String color = animal.getColor() != null
                ? animal.getColor()
                : "не указан";

        String age = animal.getAge() + "";

        String kind = animal.getKind() != null
                ? animal.getKind()
                : "не указана";


        String info = String.format(
                """
                        🐾 <b>%s</b> — <i>%s</i>
                        
                        📋 <b>Описание:</b> %s
                        🎨 <b>Окрас:</b> %s
                        📅 <b>Возраст:</b> %s лет
                        🐕 <b>Порода:</b> %s""", name, status, description, color, age, kind);

        String photoUrl = animal.getPhotoUrl();

        if (photoUrl == null) {
            messageProvider.changeText(chatId, messageId, info);
            messageProvider.changeInline(chatId, messageId, MarkUps.
                    getAnimal("backToAnimals 1", animalId, chatId));
            return;
        }

        messageProvider.deleteMessage(chatId, messageId);
        messageProvider.sendPhoto(chatId, info, animal.getPhotoUrl(), MarkUps.
                getAnimal("backToAnimalsDel 1", animalId, chatId));
    }

    /**
     * Обрабатывает заявку на взятие животного, отправляет ее волонтерам для дальнейшей обработки.
     *
     * @param chatId Идентификатор чата, из которого поступила заявка.
     * @param messageId Идентификатор сообщения, которое будет удалено.
     * @param adoptChatId Идентификатор чата, куда отправляется заявка.
     */
    public void reactionOnTakeAnimal(long chatId, int messageId, long adoptChatId) {
        messageProvider.deleteMessage(chatId, messageId);
        List<Volunteers> volunteers = volunteersService.getVolunteers();
        if (volunteers.isEmpty()) {
            messageProvider.putMessage(chatId, "Сейчас нет волонтеров!");
            return;
        }
        Random rand = new Random();
        Volunteers volunteer = volunteers.get(rand.nextInt(volunteers.size()));
        TelegramUser telegramUser = telegramUserService.getTelegramUserByTelegramId(chatId);

        if (telegramUser.getContact() == null) {
            messageProvider.putMessage(chatId, "Добавьте номер!");
            return;
        }

        messageProvider.putMessageWithMarkUps(volunteer.getTelegramId(),
                "Заявка от " + telegramUser.getContact(),
                MarkUps.acceptReject(chatId,
                        adoptChatId,
                        telegramUser.getName(),
                        telegramUser.getContact(),
                        "acceptA",
                        "rejectA"
                ));
        messageProvider.putMessage(chatId, "Ваша заявка отправлена на рассмотрение!");
    }

    public void reactionsOnSendReport(long chatId){
        Adopter adopter = adopterService.findAdopterByTelegramId(chatId);
        if ((adopter.getAnimal() != null) && !(adopter.isSendReport())) {
            statesStorage.getSendReport().put(chatId, true);
            messageProvider.putMessage(chatId, """
                                Пожалуйста, отправьте фотографию животного и добавьте к ней отчет, включающий следующие пункты:
                                - *Фото животного.*
                                - *Описание рациона питания.*
                                - *Общее самочувствие и адаптация к новому месту.*
                                - *Изменения в поведении: отказ от старых привычек или появление новых.*
                                """);
        } else if (adopter.getAnimal() != null) {
            messageProvider.putMessage(chatId, "Сегодня вы уже отправляли отчет");
        } else {
            messageProvider.putMessage(chatId, "Вы еще не взяли животное из нашего приюта!?");
        }
    }


    /**
     * Обрабатывает принятие заявки на усыновление животного.
     *
     * @param chatId Идентификатор чата, из которого пришел запрос.
     * @param messageId Идентификатор сообщения, которое будет удалено.
     * @param call_split_data Массив данных, содержащий информацию о заявителе и животном.
     */
    public void reactionsOnAccept(long chatId, int messageId, String[] call_split_data) {
        long telegramId = Long.parseLong(call_split_data[1]);
        String name = call_split_data[3];
        String contact = call_split_data[4];
        Adopter adopter = adopterService.findAdopterByTelegramId(telegramId);

        if (adopter == null) {
            adopter = new Adopter();
            adopter.setTelegramId(telegramId);
            adopter.setName(name);
            adopter.setContact(contact);
            adopter = adopterService.putAdopter(adopter);
        } else if (adopter.getAnimal() != null) {
            messageProvider.putMessage(chatId, "Ты не можешь взять больше 1 животного в пробный период");
        }

        Animal animal = animalService.getAnimalById(Long.parseLong(call_split_data[2]));

        adopter.setAnimal(animal);

        animal.setAdopter(adopter);

        animalService.putAnimal(animal);

        adopterService.putAdopter(adopter);

        messageProvider.deleteMessage(chatId, messageId);

        messageProvider.putMessage(telegramId, "Вашу заявку приняли!");
    }
}
