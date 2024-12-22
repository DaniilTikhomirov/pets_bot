package com.bot.pets_bot.controllers.bot;

import com.bot.pets_bot.config.BotConfig;
import com.bot.pets_bot.mark_ups.MarkUps;
import com.bot.pets_bot.models.dto.ReportsDTO;
import com.bot.pets_bot.models.dto.TelegramUserDTO;
import com.bot.pets_bot.models.entity.*;
import com.bot.pets_bot.services.*;
import com.bot.pets_bot.telegram_utils.MessageProvider;
import com.bot.pets_bot.telegram_utils.ModelsHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


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
    private final ShelterInfoService shelterInfoService;
    private final AnimalService animalService;
    private final AnimalAdviceService animalAdviceService;
    private final DogHandlerService dogHandlerService;
    private final AdopterService adopterService;
    private final Map<Long, Boolean> sendReport;
    private final ReportsService reportsService;

    @Value("${my.shelter.id}")
    private long SHELTER_ID;

    @Value("${my.animals.advice.id}")
    private long ANIMALS_ADVICE_ID;


    /**
     * Информация о приюте, которая отправляется пользователям.
     */
    private final String INFO_ABOUT_SHELTER = """
            <b>О нашем приюте</b>
            
            Наш приют — это место, где животные получают заботу, любовь и шанс на новую жизнь. 🐾
            Мы занимаемся спасением, лечением и поиском дома для бездомных животных.
            
            Каждый питомец, который оказывается у нас, окружён вниманием и заботой.
            Мы стремимся сделать мир добрее, помогая тем, кто не может помочь себе сам. ❤️""";

    /**
     * Конструктор для инициализации бота с необходимыми сервисами.
     *
     * @param botConfig Конфигурация бота.
     * @param telegramUserService Сервис для работы с пользователями Telegram.
     * @param messageProvider Провайдер сообщений для отправки сообщений пользователю.
     * @param volunteersService Сервис для работы с волонтерами.
     * @param shelterInfoService Сервис для получения информации о приюте.
     * @param animalService Сервис для работы с животными.
     * @param animalAdviceService Сервис для получения советов по уходу за животными.
     * @param dogHandlerService Сервис для работы с кинологами.
     * @param adopterService Сервис для работы с усыновителями.
     * @param reportsService Сервис для работы с отчетами.
     */
    public ShelterBot(BotConfig botConfig,
                      TelegramUserService telegramUserService,
                      MessageProvider messageProvider,
                      VolunteersService volunteersService,
                      ShelterInfoService shelterInfoService,
                      AnimalService animalService,
                      AnimalAdviceService animalAdviceService,
                      DogHandlerService dogHandlerService,
                      AdopterService adopterService,
                      ReportsService reportsService) {
        super(botConfig.getBotToken());
        this.botConfig = botConfig;
        this.telegramUserService = telegramUserService;
        this.messageProvider = messageProvider;
        this.volunteersService = volunteersService;
        this.shelterInfoService = shelterInfoService;
        this.animalService = animalService;
        this.animalAdviceService = animalAdviceService;
        this.dogHandlerService = dogHandlerService;
        this.adopterService = adopterService;
        this.sendReport = new ConcurrentHashMap<>();
        this.reportsService = reportsService;
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
                && sendReport.containsKey(update.getMessage().getChatId())
                && sendReport.get(update.getMessage().getChatId())) {
            Message message = update.getMessage();
            long chat_id = message.getChatId();

            List<Volunteers> volunteers = volunteersService.getVolunteers();

            if (volunteers.isEmpty()) {
                messageProvider.putMessage(chat_id, "Сейчас волонтеры отсутствуют! Мы это исправляем");
                return;
            }

            if (!message.hasPhoto()) {
                messageProvider.putMessage(chat_id, """
                        В вашем отчете отсутствует фотография""");
                return;
            }

            if (message.getCaption() == null) {
                messageProvider.putMessage(chat_id, """
                        В вашем отчете отсутствует информация\
                        - *Описание рациона питания.*
                        - *Общее самочувствие и адаптация к новому месту.*
                        - *Изменения в поведении: отказ от старых привычек или появление новых.*""");
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

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(volunteer.getTelegramId());
            sendPhoto.setPhoto(new InputFile(fileId));
            sendPhoto.setCaption("Отчет от: " + adopter.getName() + "\n" +
                    message.getCaption());
            sendPhoto.setReplyMarkup(MarkUps.acceptReject(adopter.getTelegramId(), adopter.getAnimal().getId(),
                    adopter.getName(),
                    adopter.getContact(),
                    "acceptR " + reports.getId(),
                    "rejectR " + reports.getId()));

            try {
                execute(sendPhoto);
                messageProvider.putMessage(chat_id, "Отчет отправлен! Для продолжения напишите /start");
                sendReport.remove(chat_id);
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            log.trace(message);


            if (message.equals("/start")) {
                sendReport.remove(chatId);
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
            log.trace(Arrays.toString(call_split_data));

            switch (call_split_data[0]) {
                case "callVolunteer" -> {
                    reactionOnCallVolunteer(chatId, messageId, call_split_data[1]);

                }

                case "infoAboutShelter", "BackToInfoAboutShelterMenu" -> {
                    reactionOnInfoAboutShelter(chatId, messageId);
                }

                case "infoAboutTakeAnimals", "backToAnimalsAdviceMenu" -> {
                    messageProvider.changeText(chatId, messageId, "Выберете, что вы хотите узнать");
                    messageProvider.changeInline(chatId, messageId, MarkUps.getRuleForAnimals());
                }

                case "generalInformation" -> {
                    reactionOnGeneralInformation(chatId, messageId);
                }
                case "getAnimals", "next_animal", "prev_animal", "backToAnimals" -> {
                    putAnimals(chatId, messageId, Integer.parseInt(call_split_data[1]), false);
                }

                case "click_on_animal" -> {
                    infoAboutAnimal(chatId, messageId, Long.parseLong(call_split_data[1]));
                }

                case "click_on_dogHandler" -> {
                    infoAboutDogHandler(chatId, messageId, Long.parseLong(call_split_data[1]));
                }

                case "takeAnimal" -> {
                    reactionOnTakeAnimal(chatId, messageId, Long.parseLong(call_split_data[1]));
                }

                case "sendReportAboutAnimal" -> {

                    Adopter adopter = adopterService.findAdopterByTelegramId(chatId);
                    if ((adopter.getAnimal() != null) && !(adopter.isSendReport())) {
                        sendReport.put(chatId, true);
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

                case "securityPrecautions" -> {
                    reactionOnSecurityPrecautions(chatId, messageId);
                }

                case "acceptA" -> {
                    reactionsOnAccept(chatId, messageId, call_split_data);
                }

                case "acceptR" -> {
                    long telegramId = Long.parseLong(call_split_data[2]);
                    Reports reports = reportsService.getReportsById(Long.parseLong(call_split_data[1]));

                    reports.setAccepted(true);

                    reportsService.putReports(reports);

                    Adopter adopter = reports.getAdopter();

                    adopter.setStreakCounter(adopter.getStreakCounter() + 1);

                    adopter.setWarningStreakCounter(0);

                    adopter.setSendReport(true);

                    adopterService.putAdopter(adopter);

                    messageProvider.deleteMessage(chatId, messageId);
                    messageProvider.putMessage(telegramId, "Ваш отчет приняли!");
                }
                case "acceptP" -> {
                    long adopterTelegramId = Long.parseLong(call_split_data[1]);
                    Adopter adopter = adopterService.findAdopterByTelegramId(adopterTelegramId);
                    Animal animal = animalService.getAnimalById(Long.parseLong(call_split_data[2]));

                    adopter.setAnimal(null);
                    adopter.setWarningStreakCounter(0);
                    adopter.setStreakCounter(0);
                    adopter.setWarningCounter(0);
                    adopterService.putAdopter(adopter);

                    animal.setTake(true);

                    animalService.putAnimal(animal);

                    messageProvider.putMessage(adopterTelegramId, "Поздравляем с прохождением тестового периода!");

                }

                case "add30Days" -> {
                    long adopterTelegramId = Long.parseLong(call_split_data[1]);
                    Adopter adopter = adopterService.findAdopterByTelegramId(adopterTelegramId);

                    adopter.setStreakCounter(0);

                    adopterService.putAdopter(adopter);

                    messageProvider.putMessage(adopterTelegramId, "Вам добавили 30 дней к тестовому периоду");
                }

                case "add14days" -> {
                    long adopterTelegramId = Long.parseLong(call_split_data[1]);
                    Adopter adopter = adopterService.findAdopterByTelegramId(adopterTelegramId);

                    adopter.setStreakCounter(15);

                    adopterService.putAdopter(adopter);

                    messageProvider.putMessage(adopterTelegramId, "Вам добавили 14 дней к тестовому периоду");

                }

                case "rejectA" -> {
                    long telegramId = Long.parseLong(call_split_data[1]);

                    messageProvider.deleteMessage(chatId, messageId);
                    messageProvider.putMessage(telegramId, "Вашу заявку отклонили!");
                }

                case "rejectR" -> {
                    long telegramId = Long.parseLong(call_split_data[2]);

                    Reports reports = reportsService.getReportsById(Long.parseLong(call_split_data[1]));
                    Adopter adopter = reports.getAdopter();

                    adopter.setWarningStreakCounter(adopter.getWarningStreakCounter() + 1);

                    adopter.setWarningCounter(adopter.getWarningCounter() + 1);

                    adopter.setSendReport(true);

                    adopterService.putAdopter(adopter);

                    messageProvider.deleteMessage(chatId, messageId);
                    messageProvider.putMessage(telegramId, "Ваш отчет отклонили!");
                }

                case "rulesAndDocument" -> {
                    reactionsOnRulesAndDocument(chatId, messageId);
                }

                case "animalGuide" -> {
                    reactionOnAnimalGuide(chatId, messageId);
                }

                case "dogHandleAdvice" -> {
                    reactionsOnDogHandleAdvice(chatId, messageId);
                }

                case "reasonsForRefusal" -> {
                    reactionReasonsForRefusal(chatId, messageId);
                }

                case "dogHandles", "next_dogHandler", "prev_dogHandler", "backToDogHandlers" -> {
                    putDogHandler(chatId, messageId, Integer.parseInt(call_split_data[1]), false);
                }

                case "backToInfoAboutShelterMenuDel" -> {
                    messageProvider.deleteMessage(chatId, messageId);
                    messageProvider.putMessageWithMarkUps(chatId, INFO_ABOUT_SHELTER, MarkUps.InfoAboutShelterMenu());
                }

                case "backToAnimalsAdviceMenuDel" -> {
                    messageProvider.deleteMessage(chatId, messageId);
                    messageProvider.putMessageWithMarkUps(chatId, "Выберете, что вы хотите узнать",
                            MarkUps.getRuleForAnimals());
                }

                case "backToAnimalsDel" -> {
                    putAnimals(chatId, messageId, Integer.parseInt(call_split_data[1]), true);
                }

                case "backToDogHandlersDel" -> {
                    putDogHandler(chatId, messageId, Integer.parseInt(call_split_data[1]), true);
                }

                case "getContact" -> {
                    reactionOnGetContact(chatId, messageId);
                }

                case "backToMenuDel" -> {
                    messageProvider.deleteMessage(chatId, messageId);
                    messageProvider.putMessageWithMarkUps(chatId, "Выберите нужный раздел",
                            MarkUps.startMenu());
                }

                case "backToMenu" -> {
                    messageProvider.changeText(chatId, messageId, "Выберите нужный раздел");
                    messageProvider.changeInline(chatId, messageId, MarkUps.startMenu());
                }


            }

        }
        if (update.hasMessage() && update.getMessage().hasContact()) {
            Contact contact = update.getMessage().getContact();
            long chatId = update.getMessage().getChatId();
            String phoneNumber = contact.getPhoneNumber();

            messageProvider.putMessageWithMarkUps(chatId, INFO_ABOUT_SHELTER, MarkUps.InfoAboutShelterMenu(),
                    true);

            TelegramUser telegramUser = telegramUserService.getTelegramUserByTelegramId(chatId);
            telegramUser.setContact(formatingPhoneNumber(phoneNumber));

            telegramUserService.putTelegramUser(telegramUser);
        }
    }

    /**
     * Отправляет информацию о кинологе по заданному идентификатору.
     *
     * @param chatId Идентификатор чата, в который будет отправлено сообщение.
     * @param messageId Идентификатор сообщения, которое будет изменено.
     * @param dogHandlerId Идентификатор кинолога, информацию о котором нужно вывести.
     */
    private void infoAboutDogHandler(long chatId, int messageId, long dogHandlerId) {
        DogHandler dogHandler = dogHandlerService.getDogHandler(dogHandlerId);
        if (dogHandler == null) {
            messageProvider.changeText(chatId, messageId, "Возникла Ошибка");
            messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToDogHandlers 1"));
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
            messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToDogHandlers 1"));
            return;
        }

        messageProvider.deleteMessage(chatId, messageId);
        messageProvider.sendPhoto(chatId, info, photoUrl, MarkUps.backButton("backToDogHandlersDel 1"));


    }

    /**
     * Обрабатывает принятие заявки на усыновление животного.
     *
     * @param chatId Идентификатор чата, из которого пришел запрос.
     * @param messageId Идентификатор сообщения, которое будет удалено.
     * @param call_split_data Массив данных, содержащий информацию о заявителе и животном.
     */
    private void reactionsOnAccept(long chatId, int messageId, String[] call_split_data) {
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
     * Обрабатывает заявку на взятие животного, отправляет ее волонтерам для дальнейшей обработки.
     *
     * @param chatId Идентификатор чата, из которого поступила заявка.
     * @param messageId Идентификатор сообщения, которое будет удалено.
     * @param adoptChatId Идентификатор чата, куда отправляется заявка.
     */
    private void reactionOnTakeAnimal(long chatId, int messageId, long adoptChatId) {
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

    /**
     * Отправляет информацию о животном по заданному идентификатору.
     *
     * @param chatId Идентификатор чата, в который будет отправлено сообщение.
     * @param messageId Идентификатор сообщения, которое будет изменено.
     * @param animalId Идентификатор животного, информацию о котором нужно вывести.
     */
    private void infoAboutAnimal(long chatId, int messageId, long animalId) {
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
     * Отправляет список животных для отображения на странице с пагинацией.
     *
     * @param chatId Идентификатор чата, в который будет отправлено сообщение.
     * @param messageId Идентификатор сообщения, которое будет изменено.
     * @param page Номер страницы с животными.
     * @param del Указывает, нужно ли удалять старое сообщение перед отправкой нового.
     */
    private void putAnimals(long chatId, int messageId, int page, boolean del) {
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
     * Отправляет список кинологов с пагинацией.
     *
     * @param chatId Идентификатор чата, в который будет отправлено сообщение.
     * @param messageId Идентификатор сообщения, которое будет изменено.
     * @param page Номер страницы с кинологами.
     * @param del Указывает, нужно ли удалять старое сообщение перед отправкой нового.
     */
    private void putDogHandler(long chatId, int messageId, int page, boolean del) {
        AnimalsAdvice animalsAdvice = animalAdviceService.getAnimalAdviceById(ANIMALS_ADVICE_ID);
        if (reactionOnError(chatId, messageId, animalsAdvice)) {
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
                    dogHandlers));
            return;
        }
        messageProvider.changeText(chatId, messageId, "\uD83D\uDC69\u200D⚕️\uD83D\uDC15" +
                " Наши доверенные кинологи:");
        messageProvider.changeInline(chatId, messageId, MarkUps.getPageDogHandler(page,
                dogHandlers));
    }

    /**
     * Отправляет список причин отказа в передаче животного.
     *
     * @param chatId Идентификатор чата, в который будет отправлено сообщение.
     * @param messageId Идентификатор сообщения, которое будет изменено.
     */
    private void reactionReasonsForRefusal(long chatId, int messageId) {
        AnimalsAdvice animalsAdvice = animalAdviceService.getAnimalAdviceById(ANIMALS_ADVICE_ID);
        if (reactionOnError(chatId, messageId, animalsAdvice)) {
            return;
        }

        String info = String.format(
                """
                        🚫 <b>Причины отказа в передаче животного:</b>
                        Бот может предоставить вам список возможных причин, по которым вам могут отказать \
                        в забирании собаки из приюта. Эти причины могут включать:
                        
                        %s""",
                animalsAdvice.getReasonsForRefusal().trim()
        );

        messageProvider.changeText(chatId, messageId, info);
        messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToAnimalsAdviceMenu"));
    }

    /**
     * Форматирует номер телефона в специфическом формате.
     * <p>
     * Преобразует номер телефона, представленный строкой, в формат:
     * code-XXX-XXX-XXX.
     * </p>
     *
     * @param phoneNumber номер телефона в виде строки
     * @return отформатированный номер телефона
     */
    private String formatingPhoneNumber(String phoneNumber) {
        StringBuilder sb = new StringBuilder();

        sb.append(phoneNumber.charAt(phoneNumber.length() - 1)).append(phoneNumber.charAt(phoneNumber.length() - 2));
        sb.append("-");
        sb.append(phoneNumber.charAt(phoneNumber.length() - 3)).append(phoneNumber.charAt(phoneNumber.length() - 4));
        sb.append("-");
        sb.append(phoneNumber.charAt(phoneNumber.length() - 5)).append(phoneNumber.charAt(phoneNumber.length() - 6)).
                append(phoneNumber.charAt(phoneNumber.length() - 7));
        sb.append("-");
        sb.append(phoneNumber.charAt(phoneNumber.length() - 8)).append(phoneNumber.charAt(phoneNumber.length() - 9)).
                append(phoneNumber.charAt(phoneNumber.length() - 10));
        sb.append("-");

        for (int i = 11; i <= phoneNumber.length(); i++) {
            sb.append(phoneNumber.charAt(phoneNumber.length() - i));
        }

        sb.reverse();
        return sb.toString();
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
    private boolean reactionOnError(long chatId, int messageId, AnimalsAdvice animalsAdvice) {
        if (animalsAdvice == null) {
            messageProvider.changeText(chatId, messageId, "Произошла ошибка! Попробуйте позже");
            messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToAnimalsAdviceMenu"));
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
    private void reactionsOnRulesAndDocument(long chatId, int messageId) {
        AnimalsAdvice animalsAdvice = animalAdviceService.getAnimalAdviceById(ANIMALS_ADVICE_ID);
        if (reactionOnError(chatId, messageId, animalsAdvice)) {
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
        messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToAnimalsAdviceMenu"));
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
    private void reactionOnAnimalGuide(long chatId, int messageId) {
        AnimalsAdvice animalsAdvice = animalAdviceService.getAnimalAdviceById(ANIMALS_ADVICE_ID);
        if (reactionOnError(chatId, messageId, animalsAdvice)) {
            return;
        }

        String info = String.format(
                """
                         🎉 1. Рекомендации по транспортировке вашего пушистого друга:
                        %s
                           \s
                         🐾 2. Как сделать дом идеальным для щенка? Вот что нужно учесть:
                        %s
                           \s
                         🏡 3. Рекомендации по обустройству дома для вашего взрослого питомца:
                        %s
                           \s
                         ♿ 4. Советы по обустройству дома для животного с ограниченными возможностями:
                        %s
                        \s""",
                animalsAdvice.getRecommendationForMoveAnimal().trim(),
                animalsAdvice.getRecommendationForArrangementForPuppy().trim(),
                animalsAdvice.getRecommendationForArrangementForDog().trim(),
                animalsAdvice.getRecommendationForArrangementForDisability().trim()
        );


        messageProvider.changeText(chatId, messageId, info);
        messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToAnimalsAdviceMenu"));


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
    private void reactionsOnDogHandleAdvice(long chatId, int messageId) {
        AnimalsAdvice animalsAdvice = animalAdviceService.getAnimalAdviceById(ANIMALS_ADVICE_ID);
        if (reactionOnError(chatId, messageId, animalsAdvice)) {
            return;
        }

        String info = String.format(
                """
                        🐶✨ Советы от кинолога по первичному общению с вашей новой собакой:
                        %s
                        """, animalsAdvice.getDogHandlerAdvice().trim());

        messageProvider.changeText(chatId, messageId, info);
        messageProvider.changeInline(chatId, messageId, MarkUps.backButton("backToAnimalsAdviceMenu"));

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
    private void reactionOnGetContact(long chatId, int messageId) {
        messageProvider.deleteMessage(chatId, messageId);
        messageProvider.putKeyBoard(chatId, "Чтобы отправить номер нажмите кнопку под полем ввода," +
                " если не хотите отправлять нажмите /start", MarkUps.getContact());
    }

    /**
     * Реакция на запрос по вопросам безопасности и контактной информации.
     * <p>
     * Отправляет пользователю информацию о контактных данных охраны и рекомендации
     * по безопасности при посещении приюта.
     * </p>
     *
     * @param chatId идентификатор чата
     * @param messageId идентификатор сообщения
     */
    private void reactionOnSecurityPrecautions(long chatId, int messageId) {
        ShelterInfo shelterInfo = shelterInfoService.getShelterInfoById(SHELTER_ID);
        if (shelterInfo == null) {
            messageProvider.putMessage(chatId, "Возникла ошибка попробуйте позже /start");
            return;
        }

        String info = String.format("""
                <b>Контактная информация и безопасность</b>
                
                Наш бот также может предоставить вам важную информацию о безопасности и контактах:
                
                <b>🔐 Контактные данные охраны:</b>
                Для оформления пропуска на машину, свяжитесь с охраной по номеру:
                %s
                
                <b>⚠️ Рекомендации по безопасности:</b>
                %s""", shelterInfo.getSecurityContact(), shelterInfo.getSafetyPrecautions());


        messageProvider.changeText(chatId, messageId, info);
        messageProvider.changeInline(chatId, messageId, MarkUps.backButton("BackToInfoAboutShelterMenu"));

    }

    /**
     * Реакция на запрос общей информации о приюте.
     * <p>
     * Отправляет пользователю информацию о графике работы приюта, его адресе и
     * схеме проезда.
     * </p>
     *
     * @param chatId идентификатор чата
     * @param messageId идентификатор сообщения
     */
    private void reactionOnGeneralInformation(long chatId, int messageId) {
        ShelterInfo shelterInfo = shelterInfoService.getShelterInfoById(SHELTER_ID);
        if (shelterInfo == null) {
            messageProvider.putMessage(chatId, "Возникла ошибка попробуйте позже /start");
            return;
        }
        Schedules schedules = shelterInfo.getSchedules();
        if (schedules == null) {
            messageProvider.putMessage(chatId, "Возникла ошибка попробуйте позже /start");
            return;
        }

        String info = String.format("""
                        <b>Информация о приюте</b>
                        
                        Наш бот готов предоставить вам важную информацию о приюте:
                        
                        <b>\uD83D\uDCC5 Расписание работы:</b>
                           Понедельник: %s
                           Вторник: %s
                           Среда: %s
                           Четверг: %s
                           Пятница: %s
                           Суббота: %s
                           Воскресенье: %s
                        
                        <b>\uD83D\uDCCD Адрес приюта:</b>
                           %s
                        
                        <b>\uD83D\uDDFA️ Схема проезда указана на фотографии выше</b>""",
                schedules.getMonday(),
                schedules.getTuesday(),
                schedules.getWednesday(),
                schedules.getThursday(),
                schedules.getFriday(),
                schedules.getSunday(),
                schedules.getSaturday(),
                shelterInfo.getAddress());

        messageProvider.deleteMessage(chatId, messageId);

        messageProvider.sendPhoto(chatId, info, shelterInfo.getSchemaForRoadPhotoUrl(),
                MarkUps.backButton("backToInfoAboutShelterMenuDel"));


    }

    /**
     * Реакция на запрос информации о приюте.
     * <p>
     * Отправляет пользователю текстовую информацию о приюте и кнопки для перехода
     * в соответствующие разделы.
     * </p>
     *
     * @param chatId идентификатор чата
     * @param messageId идентификатор сообщения
     */
    private void reactionOnInfoAboutShelter(long chatId, int messageId) {

        messageProvider.changeText(chatId, messageId, INFO_ABOUT_SHELTER);
        messageProvider.changeInline(chatId, messageId, MarkUps.InfoAboutShelterMenu());
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
    private void reactionOnCallVolunteer(long chatId, int messageId, String back) {
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
     * Отправляет стартовое сообщение новому пользователю.
     * <p>
     * Если пользователь новый, создает запись в базе данных и отправляет приветственное
     * сообщение с предложением выбрать раздел. В противном случае просто отправляется
     * меню для выбора раздела.
     * </p>
     *
     * @param chatId идентификатор чата
     * @param name имя пользователя
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
                MarkUps.startMenu(), true);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
}
