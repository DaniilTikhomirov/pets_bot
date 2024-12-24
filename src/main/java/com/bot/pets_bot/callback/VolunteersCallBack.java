package com.bot.pets_bot.callback;

import com.bot.pets_bot.models.entity.Adopter;
import com.bot.pets_bot.models.entity.Animal;
import com.bot.pets_bot.models.entity.Reports;
import com.bot.pets_bot.reactions.VolunteersReactions;
import com.bot.pets_bot.services.AdopterService;
import com.bot.pets_bot.services.AnimalService;
import com.bot.pets_bot.services.ReportsService;
import com.bot.pets_bot.telegram_utils.MessageProvider;
import org.springframework.stereotype.Component;

@Component
public class VolunteersCallBack implements CallBackResponsive {

    private final MessageProvider messageProvider;
    private final AdopterService adopterService;
    private final ReportsService reportsService;
    private final AnimalService animalService;
    private final VolunteersReactions volunteersReactions;

    public VolunteersCallBack(MessageProvider messageProvider,
                              AdopterService adopterService,
                              ReportsService reportsService,
                              AnimalService animalService,
                              VolunteersReactions volunteersReactions) {
        this.messageProvider = messageProvider;
        this.adopterService = adopterService;
        this.reportsService = reportsService;
        this.animalService = animalService;
        this.volunteersReactions = volunteersReactions;
    }

    @Override
    public void callback(long chatId, int messageId, String[] call_split_data) {
        switch (call_split_data[0]) {
            case "callVolunteer" -> {
                volunteersReactions.reactionOnCallVolunteer(chatId, messageId, call_split_data[1]);
            }

            case "acceptA" -> {
                volunteersReactions.reactionsOnAccept(chatId, messageId, call_split_data);
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

            case "getContact" -> {
                volunteersReactions.reactionOnGetContact(chatId, messageId);
            }


        }
    }
}
