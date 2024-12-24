package com.bot.pets_bot.reactions;

import com.bot.pets_bot.mark_ups.MarkUps;
import com.bot.pets_bot.models.entity.Adopter;
import com.bot.pets_bot.models.entity.Animal;
import com.bot.pets_bot.models.entity.Volunteers;
import com.bot.pets_bot.services.AdopterService;
import com.bot.pets_bot.services.AnimalService;
import com.bot.pets_bot.services.VolunteersService;
import com.bot.pets_bot.telegram_utils.MessageProvider;
import org.springframework.stereotype.Component;

@Component
public class VolunteersReactions {

    private final MessageProvider messageProvider;
    private final VolunteersService volunteersService;
    private final AdopterService adopterService;
    private final AnimalService animalService;

    public VolunteersReactions(MessageProvider messageProvider, VolunteersService volunteersService, AdopterService adopterService, AnimalService animalService) {
        this.messageProvider = messageProvider;
        this.volunteersService = volunteersService;
        this.adopterService = adopterService;
        this.animalService = animalService;
    }

    /**
     * Реакция на запрос контакта с волонтером.
     * <p>
     * Отправляет пользователю контактную информацию случайного доступного волонтера
     * или сообщение о том, что волонтеры отсутствуют.
     * </p>
     *
     * @param chatId идентификатор чата
     * @param messageId идентификатор сообщения
     * @param back строка с ссылкой для возврата в предыдущее меню
     */
    public void reactionOnCallVolunteer(long chatId, int messageId, String back) {
        messageProvider.deleteMessage(chatId, messageId);
        Volunteers volunteers = volunteersService.getRandomVolunteer();
        if (volunteers != null && volunteers.getContact() != null) {
            messageProvider.sendContact(chatId, volunteers.getContact(), volunteers.getName(), volunteers.getSecondName(),
                    MarkUps.backButton(back));
            return;
        }
        messageProvider.putMessageWithMarkUps(chatId, "на данный момент нет свободных волонтеров",
                MarkUps.backButton(back));
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

    /**
     * Реакция на запрос контакта.
     * <p>
     * Отправляет пользователю кнопку для получения контактной информации.
     * </p>
     *
     * @param chatId идентификатор чата
     * @param messageId идентификатор сообщения
     */
    public void reactionOnGetContact(long chatId, int messageId) {
        messageProvider.deleteMessage(chatId, messageId);
        messageProvider.putKeyBoard(chatId, "Чтобы отправить номер нажмите кнопку под полем ввода," +
                " если не хотите отправлять нажмите /start", MarkUps.getContact());
    }



}
