package com.bot.pets_bot.callback;

public interface CallBackResponsive {

    void callback(long chatId, int messageId, String[] call_split_data, String prefix);
}
