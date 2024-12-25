package com.bot.pets_bot.callback;

import com.bot.pets_bot.mark_ups.MarkUps;
import com.bot.pets_bot.reactions.AnimalAdviceReactions;
import com.bot.pets_bot.telegram_utils.MessageProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AnimalsAdviceCallBack implements CallBackResponsive {
    private final MessageProvider messageProvider;
    private final AnimalAdviceReactions animalAdviceReactions;

    @Value("${my.dog.advice.id}")
    private long DOG_ADVICE_ID;

    @Value("${my.cat.advice.id}")
    private long CAT_ADVICE_ID;

    public AnimalsAdviceCallBack(MessageProvider messageProvider,
                                 AnimalAdviceReactions animalAdviceReactions) {
        this.messageProvider = messageProvider;
        this.animalAdviceReactions = animalAdviceReactions;
    }

    @Override
    public void callback(long chatId, int messageId, String[] call_split_data, String prefix) {
        switch (call_split_data[0]) {
            case "infoAboutTakeAnimals", "backToAnimalsAdviceMenu" -> {
                messageProvider.changeText(chatId, messageId, "Выберете, что вы хотите узнать");
                messageProvider.changeInline(chatId, messageId, MarkUps.getRuleForAnimals(prefix));
            }
            case "rulesAndDocument" -> {
                if (prefix.equals("dog")) {
                    animalAdviceReactions.reactionsOnRulesAndDocument(chatId, messageId, DOG_ADVICE_ID, prefix);
                } else if (prefix.equals("cat")) {
                    animalAdviceReactions.reactionsOnRulesAndDocument(chatId, messageId, CAT_ADVICE_ID, prefix);
                }
            }

            case "animalGuide" -> {
                if (prefix.equals("dog")) {
                    animalAdviceReactions.reactionOnAnimalGuide(chatId, messageId, DOG_ADVICE_ID, prefix);
                } else if (prefix.equals("cat")) {
                    animalAdviceReactions.reactionOnAnimalGuide(chatId, messageId, CAT_ADVICE_ID, prefix);
                }

            }

            case "dogHandleAdvice" -> {
                if (prefix.equals("dog")) {
                    animalAdviceReactions.reactionsOnDogHandleAdvice(chatId, messageId, DOG_ADVICE_ID, prefix);
                } else if (prefix.equals("cat")) {
                    animalAdviceReactions.reactionsOnDogHandleAdvice(chatId, messageId, CAT_ADVICE_ID, prefix);
                }


            }

            case "reasonsForRefusal" -> {

                if (prefix.equals("dog")) {
                    animalAdviceReactions.reactionReasonsForRefusal(chatId, messageId, DOG_ADVICE_ID, prefix);
                } else if (prefix.equals("cat")) {
                    animalAdviceReactions.reactionReasonsForRefusal(chatId, messageId, CAT_ADVICE_ID, prefix);
                }

            }

            case "dogHandles", "next_dogHandler", "prev_dogHandler", "backToDogHandlers" -> {

                if (prefix.equals("dog")) {
                    animalAdviceReactions.putDogHandler(chatId, messageId, Integer.parseInt(call_split_data[1]),
                            DOG_ADVICE_ID, false, prefix);
                } else if (prefix.equals("cat")) {
                    animalAdviceReactions.putDogHandler(chatId, messageId, Integer.parseInt(call_split_data[1]),
                            CAT_ADVICE_ID, false, prefix);
                }

            }

            case "click_on_dogHandler" -> {
                animalAdviceReactions.infoAboutDogHandler(chatId, messageId, Long.parseLong(call_split_data[1]), prefix);
            }

            case "backToAnimalsAdviceMenuDel" -> {
                messageProvider.deleteMessage(chatId, messageId);
                messageProvider.putMessageWithMarkUps(chatId, "Выберете, что вы хотите узнать",
                        MarkUps.getRuleForAnimals(prefix));
            }

            case "backToDogHandlersDel" -> {

                if (prefix.equals("dog")) {
                    animalAdviceReactions.putDogHandler(chatId, messageId,
                            Integer.parseInt(call_split_data[1]), DOG_ADVICE_ID, true, prefix);
                } else if (prefix.equals("cat")) {
                    animalAdviceReactions.putDogHandler(chatId, messageId,
                            Integer.parseInt(call_split_data[1]), CAT_ADVICE_ID, true, prefix);
                }

            }
        }
    }
}
