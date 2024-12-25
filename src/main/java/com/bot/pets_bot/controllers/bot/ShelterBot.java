package com.bot.pets_bot.controllers.bot;

import com.bot.pets_bot.callback.AnimalsAdviceCallBack;
import com.bot.pets_bot.callback.AnimalsCallBack;
import com.bot.pets_bot.callback.ShelterInfoCallBack;
import com.bot.pets_bot.callback.VolunteersCallBack;
import com.bot.pets_bot.config.BotConfig;
import com.bot.pets_bot.mark_ups.MarkUps;
import com.bot.pets_bot.models.dto.ReportsDTO;
import com.bot.pets_bot.models.dto.TelegramUserDTO;
import com.bot.pets_bot.models.entity.*;
import com.bot.pets_bot.services.*;
import com.bot.pets_bot.telegram_utils.MessageProvider;
import com.bot.pets_bot.telegram_utils.StatesStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.*;

import java.io.File;
import java.util.*;


/**
 * Класс для обработки сообщений и взаимодействия с пользователями через Telegram-бота приюта.
 * Этот класс управляет обработкой команд бота, включая отправку отчетов, запросы информации о приюте,
 * а также взаимодействие с волонтерами и усыновителями животных.
 */
@Slf4j
@Component
public class ShelterBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final TelegramUserService telegramUserService;
    private final MessageProvider messageProvider;
    private final VolunteersService volunteersService;
    private final AdopterService adopterService;
    private final ReportsService reportsService;
    private final StatesStorage statesStorage;
    private final ShelterInfoCallBack shelterInfoCallBack;
    private final AnimalsCallBack animalsCallBack;
    private final AnimalsAdviceCallBack animalsAdviceCallBack;
    private final VolunteersCallBack volunteersCallBack;


    /**
     * Конструктор для инициализации бота с необходимыми сервисами.
     *
     * @param botConfig           Конфигурация бота.
     * @param telegramUserService Сервис для работы с пользователями Telegram.
     * @param messageProvider     Провайдер сообщений для отправки сообщений пользователю.
     * @param volunteersService   Сервис для работы с волонтерами.
     * @param adopterService      Сервис для работы с усыновителями.
     * @param reportsService      Сервис для работы с отчетами.
     */
    public ShelterBot(BotConfig botConfig,
                      TelegramUserService telegramUserService,
                      MessageProvider messageProvider,
                      VolunteersService volunteersService,
                      AdopterService adopterService,
                      ReportsService reportsService,
                      StatesStorage statesStorage,
                      ShelterInfoCallBack shelterInfoCallBack,
                      AnimalsCallBack animalsCallBack,
                      AnimalsAdviceCallBack animalsAdviceCallBack,
                      VolunteersCallBack volunteersCallBack) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.telegramUserService = telegramUserService;
        this.messageProvider = messageProvider;
        this.volunteersService = volunteersService;
        this.adopterService = adopterService;
        this.reportsService = reportsService;
        this.statesStorage = statesStorage;
        this.shelterInfoCallBack = shelterInfoCallBack;
        this.animalsCallBack = animalsCallBack;
        this.animalsAdviceCallBack = animalsAdviceCallBack;
        this.volunteersCallBack = volunteersCallBack;
    }


    /**
     * Обрабатывает полученные обновления от Telegram. Этот метод управляет взаимодействием с пользователем,
     * включая обработку команд и сообщений.
     *
     * @param update Обновление, полученное от Telegram.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()
                && statesStorage.getSendReport().containsKey(update.getMessage().getChatId())
                && statesStorage.getSendReport().get(update.getMessage().getChatId())) {
            Message message = update.getMessage();
            long chat_id = message.getChatId();

            sendReport(chat_id, message);


        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            log.trace(message);


            if (message.equals("/start")) {
                statesStorage.getSendReport().remove(chatId);
                startMessage(chatId, update.getMessage().getChat().getFirstName());
            } else {
                messageProvider.putMessage(chatId, "Неизвестная команда!");
            }
        }

        if (update.hasCallbackQuery()) {
            String callbackQuery = update.getCallbackQuery().getData();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            String[] call_split_data = callbackQuery.split(" ");
            String prefix = call_split_data[0].split("\\|")[0];
            call_split_data[0] = call_split_data[0].split("\\|")[1];
            log.info(Arrays.toString(call_split_data));

            if(call_split_data[0].equals("dog") || call_split_data[0].equals("cat")){
                messageProvider.changeInline(chatId, messageId, MarkUps.startMenu(prefix));
            }else if(call_split_data[0].equals("backToStart")){
                messageProvider.changeInline(chatId, messageId, MarkUps.catOrDog());
            }

            animalsCallBack.callback(chatId, messageId, call_split_data, prefix);
            animalsAdviceCallBack.callback(chatId, messageId, call_split_data, prefix);
            shelterInfoCallBack.callback(chatId, messageId, call_split_data, prefix);
            volunteersCallBack.callback(chatId, messageId, call_split_data, prefix);

        }
        if (update.hasMessage() && update.getMessage().hasContact()) {
            Contact contact = update.getMessage().getContact();
            long chatId = update.getMessage().getChatId();
            String phoneNumber = contact.getPhoneNumber();

            shelterInfoCallBack.addContact(chatId, phoneNumber);
        }
    }


    private boolean checkingReport(long chat_id, List<Volunteers> volunteers, Message message) {
        if (volunteers.isEmpty()) {
            messageProvider.putMessage(chat_id, "Сейчас волонтеры отсутствуют! Мы это исправляем");
            return false;
        }

        if (!message.hasPhoto()) {
            messageProvider.putMessage(chat_id, """
                    В вашем отчете отсутствует фотография""");
            return false;
        }

        if (message.getCaption() == null) {
            messageProvider.putMessage(chat_id, """
                    В вашем отчете отсутствует информация\
                    - *Описание рациона питания.*
                    - *Общее самочувствие и адаптация к новому месту.*
                    - *Изменения в поведении: отказ от старых привычек или появление новых.*""");
            return false;
        }

        return true;
    }

    private void sendReport(long chat_id, Message message) {
        List<Volunteers> volunteers = volunteersService.getVolunteers();

        if (!checkingReport(chat_id, volunteers, message)) {
            return;
        }


        Random random = new Random();
        Volunteers volunteer = volunteers.get(random.nextInt(volunteers.size()));

        Adopter adopter = adopterService.findAdopterByTelegramId(chat_id);


        List<PhotoSize> photoSizes = message.getPhoto();

        PhotoSize photoSize = photoSizes.getLast();

        String fileId = photoSize.getFileId();
        ReportsDTO reportsDTO = new ReportsDTO();

        reportsDTO.setText(message.getCaption());
        reportsDTO.setAdopterId(adopter.getId());
        reportsDTO.setVolunteerId(volunteer.getId());
        reportsDTO.setAnimalId(adopter.getAnimal().getId());

        Reports reports = reportsService.addReports(reportsDTO);

        File file = messageProvider.getFile(fileId, chat_id + "");

        reportsService.addPhoto(file, reports.getId());

        Message photo = messageProvider.sendPhoto(volunteer.getTelegramId(),
                "Отчет от: " + adopter.getName() + "\n" +
                        message.getCaption(), new InputFile(fileId), MarkUps.acceptReject(adopter.getTelegramId(),
                        adopter.getAnimal().getId(),
                        adopter.getName(),
                        adopter.getContact(),
                        "acceptR " + reports.getId(),
                        "rejectR " + reports.getId(), "report"));

        if (photo != null) {
            messageProvider.putMessage(chat_id, "Отчет отправлен! Для продолжения напишите /start");
            statesStorage.getSendReport().remove(chat_id);
        }
    }


    /**
     * Отправляет стартовое сообщение новому пользователю.
     * <p>
     * Если пользователь новый, создает запись в базе данных и отправляет приветственное
     * сообщение с предложением выбрать раздел. В противном случае просто отправляется
     * меню для выбора раздела.
     * </p>
     *
     * @param chatId идентификатор чата
     * @param name   имя пользователя
     */
    private void startMessage(long chatId, String name) {
        if (telegramUserService.isNewUserByTelegramId(chatId)) {
            TelegramUserDTO telegramUser = new TelegramUserDTO();
            telegramUser.setTelegramId(chatId);
            telegramUser.setName(name);
            telegramUserService.addTelegramUser(telegramUser);
            messageProvider.putMessage(chatId, "Добро пожаловать " + name, true);
        }

        messageProvider.putMessageWithMarkUps(chatId, "Выберите нужный раздел",
                MarkUps.catOrDog(), true);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
}
