package com.bot.pets_bot.reactions;

import com.bot.pets_bot.mark_ups.MarkUps;
import com.bot.pets_bot.models.entity.Schedules;
import com.bot.pets_bot.models.entity.ShelterInfo;
import com.bot.pets_bot.services.ShelterInfoService;
import com.bot.pets_bot.telegram_utils.MessageProvider;
import org.springframework.stereotype.Component;

@Component
public class ShelterInfoReactions {

    private final MessageProvider messageProvider;
    private final ShelterInfoService shelterInfoService;

    public ShelterInfoReactions(MessageProvider messageProvider, ShelterInfoService shelterInfoService) {
        this.messageProvider = messageProvider;
        this.shelterInfoService = shelterInfoService;
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
    public void reactionOnInfoAboutShelter(long chatId, int messageId, String INFO_ABOUT_SHELTER, String prefix) {

        messageProvider.changeText(chatId, messageId, INFO_ABOUT_SHELTER);
        messageProvider.changeInline(chatId, messageId, MarkUps.InfoAboutShelterMenu(prefix));
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
    public void reactionOnGeneralInformation(long chatId, int messageId, long SHELTER_ID, String prefix) {
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
                MarkUps.backButton("backToInfoAboutShelterMenuDel", prefix));


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
    public void reactionOnSecurityPrecautions(long chatId, int messageId, long SHELTER_ID, String prefix) {
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
        messageProvider.changeInline(chatId, messageId, MarkUps.backButton("BackToInfoAboutShelterMenu", prefix));

    }
}
