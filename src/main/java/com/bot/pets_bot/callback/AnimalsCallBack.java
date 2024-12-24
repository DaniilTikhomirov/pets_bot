package com.bot.pets_bot.callback;

import com.bot.pets_bot.mark_ups.MarkUps;
import com.bot.pets_bot.telegram_utils.MessageProvider;
import org.springframework.stereotype.Component;
import com.bot.pets_bot.reactions.AnimalsReactions;

@Component
public class AnimalsCallBack implements CallBackResponsive{

    private final MessageProvider messageProvider;
    private final AnimalsReactions animalsReactions;

    public AnimalsCallBack(MessageProvider messageProvider,
                           AnimalsReactions animalsReactions) {
        this.messageProvider = messageProvider;
        this.animalsReactions = animalsReactions;
    }

    @Override
    public void callback(long chatId, int messageId, String[] call_split_data) {
        switch (call_split_data[0]) {
            case "getAnimals", "next_animal", "prev_animal", "backToAnimals" -> {
                animalsReactions.putAnimals(chatId, messageId, Integer.parseInt(call_split_data[1]), false);
            }

            case "click_on_animal" -> {
                animalsReactions.infoAboutAnimal(chatId, messageId, Long.parseLong(call_split_data[1]));
            }

            case "takeAnimal" -> {
                animalsReactions.reactionOnTakeAnimal(chatId, messageId, Long.parseLong(call_split_data[1]));
            }

            case "sendReportAboutAnimal" -> {
                animalsReactions.reactionsOnSendReport(chatId);
            }

            case "acceptA" -> {
                animalsReactions.reactionsOnAccept(chatId, messageId, call_split_data);
            }

            case "rejectA" -> {
                long telegramId = Long.parseLong(call_split_data[1]);

                messageProvider.deleteMessage(chatId, messageId);
                messageProvider.putMessage(telegramId, "Вашу заявку отклонили!");
            }

            case "backToAnimalsDel" -> {
                animalsReactions.putAnimals(chatId, messageId, Integer.parseInt(call_split_data[1]), true);
            }
        }
    }
}
