package com.bot.pets_bot.callback;

import com.bot.pets_bot.mark_ups.MarkUps;
import com.bot.pets_bot.reactions.AnimalAdviceReactions;
import com.bot.pets_bot.telegram_utils.MessageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AnimalsAdviceCallBack implements CallBackResponsive{
    private final MessageProvider messageProvider;
    private final AnimalAdviceReactions animalAdviceReactions;

    @Value("${my.animals.advice.id}")
    private long ANIMALS_ADVICE_ID;

    public AnimalsAdviceCallBack(MessageProvider messageProvider,
                                 AnimalAdviceReactions animalAdviceReactions) {
        this.messageProvider = messageProvider;
        this.animalAdviceReactions = animalAdviceReactions;
    }

    @Override
    public void callback(long chatId, int messageId, String[] call_split_data) {
        switch (call_split_data[0]) {
            case "infoAboutTakeAnimals", "backToAnimalsAdviceMenu" -> {
                messageProvider.changeText(chatId, messageId, "Выберете, что вы хотите узнать");
                messageProvider.changeInline(chatId, messageId, MarkUps.getRuleForAnimals());
            }
            case "rulesAndDocument" -> {
                animalAdviceReactions.reactionsOnRulesAndDocument(chatId, messageId, ANIMALS_ADVICE_ID);
            }

            case "animalGuide" -> {
               animalAdviceReactions.reactionOnAnimalGuide(chatId, messageId, ANIMALS_ADVICE_ID);
            }

            case "dogHandleAdvice" -> {
                animalAdviceReactions.reactionsOnDogHandleAdvice(chatId, messageId, ANIMALS_ADVICE_ID);
            }

            case "reasonsForRefusal" -> {
                animalAdviceReactions.reactionReasonsForRefusal(chatId, messageId, ANIMALS_ADVICE_ID);
            }

            case "dogHandles", "next_dogHandler", "prev_dogHandler", "backToDogHandlers" -> {
                animalAdviceReactions.putDogHandler(chatId, messageId, Integer.parseInt(call_split_data[1]),
                        ANIMALS_ADVICE_ID,false);
            }

            case "click_on_dogHandler" -> {
                animalAdviceReactions.infoAboutDogHandler(chatId, messageId, Long.parseLong(call_split_data[1]));
            }

            case "backToAnimalsAdviceMenuDel" -> {
                messageProvider.deleteMessage(chatId, messageId);
                messageProvider.putMessageWithMarkUps(chatId, "Выберете, что вы хотите узнать",
                        MarkUps.getRuleForAnimals());
            }

            case "backToDogHandlersDel" -> {
                animalAdviceReactions.putDogHandler(chatId, messageId, Integer.parseInt(call_split_data[1]), ANIMALS_ADVICE_ID,true);
            }
        }
    }
}
