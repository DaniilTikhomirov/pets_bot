package com.bot.pets_bot.callback;

import com.bot.pets_bot.mark_ups.MarkUps;
import com.bot.pets_bot.models.entity.TelegramUser;
import com.bot.pets_bot.reactions.ShelterInfoReactions;
import com.bot.pets_bot.services.TelegramUserService;
import com.bot.pets_bot.telegram_utils.MessageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShelterInfoCallBack implements CallBackResponsive{

    private final ShelterInfoReactions shelterInfoReactions;
    private final MessageProvider messageProvider;
    private final TelegramUserService telegramUserService;

    /**
     * Информация о приюте, которая отправляется пользователям.
     */
    private final String INFO_ABOUT_SHELTER = """
            <b>О нашем приюте</b>
            
            Наш приют — это место, где животные получают заботу, любовь и шанс на новую жизнь. 🐾
            Мы занимаемся спасением, лечением и поиском дома для бездомных животных.
            
            Каждый питомец, который оказывается у нас, окружён вниманием и заботой.
            Мы стремимся сделать мир добрее, помогая тем, кто не может помочь себе сам. ❤️""";


    @Value("${my.shelter.dog.id}")
    private long DOG_SHELTER_ID;

    @Value("${my.shelter.cat.id}")
    private long CAT_SHELTER_ID;

    public ShelterInfoCallBack(ShelterInfoReactions shelterInfoReactions,
                               MessageProvider messageProvider,
                               TelegramUserService telegramUserService) {
        this.shelterInfoReactions = shelterInfoReactions;
        this.messageProvider = messageProvider;
        this.telegramUserService = telegramUserService;
    }



    @Override
    public void callback(long chatId, int messageId, String[] call_split_data, String prefix) {
        switch (call_split_data[0]){
            case "infoAboutShelter", "BackToInfoAboutShelterMenu" -> {
                shelterInfoReactions.reactionOnInfoAboutShelter(chatId, messageId, INFO_ABOUT_SHELTER, prefix);
            }

            case "generalInformation" -> {
                if(prefix.equals("cat")){
                    shelterInfoReactions.reactionOnGeneralInformation(chatId, messageId, CAT_SHELTER_ID, prefix);
                }else if(prefix.equals("dog")){
                    shelterInfoReactions.reactionOnGeneralInformation(chatId, messageId, DOG_SHELTER_ID, prefix);
                }

            }

            case "securityPrecautions" -> {

                if(prefix.equals("cat")){
                    shelterInfoReactions.reactionOnSecurityPrecautions(chatId, messageId, CAT_SHELTER_ID, prefix);
                } else if (prefix.equals("dog")) {
                    shelterInfoReactions.reactionOnSecurityPrecautions(chatId, messageId, DOG_SHELTER_ID, prefix);
                }

            }
            case "backToInfoAboutShelterMenuDel" -> {
                messageProvider.deleteMessage(chatId, messageId);
                messageProvider.putMessageWithMarkUps(chatId, INFO_ABOUT_SHELTER, MarkUps.InfoAboutShelterMenu(prefix));
            }

            case "backToMenuDel" -> {
                messageProvider.deleteMessage(chatId, messageId);
                messageProvider.putMessageWithMarkUps(chatId, "Выберите нужный раздел",
                        MarkUps.startMenu(prefix));
            }

            case "backToMenu" -> {
                messageProvider.changeText(chatId, messageId, "Выберите нужный раздел");
                messageProvider.changeInline(chatId, messageId, MarkUps.startMenu(prefix));
            }


        }
    }

    public void addContact(long chatId, String phoneNumber){
        messageProvider.putMessageWithMarkUps(chatId, "Выберете приют", MarkUps.catOrDog(),
                true);

        TelegramUser telegramUser = telegramUserService.getTelegramUserByTelegramId(chatId);
        telegramUser.setContact(formatingPhoneNumber(phoneNumber));

        telegramUserService.putTelegramUser(telegramUser);
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
}
