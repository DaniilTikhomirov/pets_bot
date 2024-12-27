package com.bot.pets_bot.controllers.bot;

import com.bot.pets_bot.mark_ups.MarkUps;
import com.bot.pets_bot.models.entity.Adopter;
import com.bot.pets_bot.models.entity.Volunteers;
import com.bot.pets_bot.services.AdopterService;
import com.bot.pets_bot.services.VolunteersService;
import com.bot.pets_bot.telegram_utils.MessageProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Random;

/**
 * Класс, обеспечивающий работу с планировщиком задач в Telegram-боте.
 * <p>
 * Этот класс выполняет задачи, связанные с уведомлениями для пользователей (усыновителей) и волонтеров
 * через Telegram-бота, с использованием планировщика задач, включающего уведомления и проверки состояния
 * отчетности усыновителей.
 * </p>
 * <p>
 * Включает два запланированных задания:
 * <ul>
 *     <li>Уведомление для усыновителей о необходимости отправить отчет.</li>
 *     <li>Проверка отчетности усыновителей и отправка уведомлений волонтерам о нарушениях.</li>
 * </ul>
 * </p>
 */
@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class TelegramScheduler {

    private final AdopterService adopterService;
    private final MessageProvider messageProvider;
    private final VolunteersService volunteersService;

    /**
     * Конструктор для инициализации {@link TelegramScheduler}.
     *
     * @param adopterService    Сервис для работы с усыновителями.
     * @param messageProvider   Сервис для отправки сообщений пользователю.
     * @param volunteersService Сервис для работы с волонтерами.
     */
    public TelegramScheduler(AdopterService adopterService, MessageProvider messageProvider, VolunteersService volunteersService) {
        this.adopterService = adopterService;
        this.messageProvider = messageProvider;
        this.volunteersService = volunteersService;
    }

    /**
     * Запланированная задача для отправки уведомлений усыновителям о необходимости отправить отчет.
     * <p>
     * Задача выполняется каждый день в 10:00 и напоминает усыновителям, что им необходимо отправить отчет.
     * </p>
     */
    @Scheduled(cron = "0 0 10 * * *")
    public void notification() {
        List<Adopter> adopters = adopterService.getAllAdopters();
        adopters.stream().filter(o -> o.getAnimal() != null || !(o.isSendReport())).forEach(o -> {
            messageProvider.putMessage(o.getTelegramId(), "Не забудьте сегодня отправить отчет!");
        });
    }

    /**
     * Запланированная задача для проверки состояния отчетности усыновителей и отправки уведомлений волонтерам.
     * <p>
     * Задача выполняется каждый день в 23:50 и проверяет, какие усыновители не отправили отчет в течение
     * длительного времени и отправляет уведомления соответствующим волонтерам о нарушениях. Если усыновитель
     * успешно прошел тестовый этап, волонтеру также отправляется информация об этом.
     * </p>
     */
    @Scheduled(cron = "0 50 23 * * *")
    public void check() {
        List<Adopter> adopters = adopterService.getAllAdopters();
        List<Volunteers> volunteers = volunteersService.getVolunteers();
        Random random = new Random();
        adopters.forEach(o -> {
            Volunteers volunteer = volunteers.get(random.nextInt(volunteers.size()));
            if(o.getAnimal() != null && o.getStreakCounter() > 1){
                messageProvider.putMessage(volunteer.getTelegramId(), "пользователь " + o.getName() +
                        " не отправял отчет уже " + o.getWarningStreakCounter() + " телефон: " + o.getContact());
            }

            if(o.getAnimal() != null && o.getStreakCounter() >= 29){
                String info = String.format("""
                        Пользователь %s прошел тестовый этап!
                        Нарушений: %s
                        """, o.getName(), o.getWarningCounter());
                messageProvider.putMessageWithMarkUps(volunteer.getTelegramId(), info,
                        MarkUps.addDaysOrAccept(o.getTelegramId(), o.getAnimal().getId(), "report"));
            }

            o.setSendReport(false);

            adopterService.putAdopter(o);
        });
    }
}
