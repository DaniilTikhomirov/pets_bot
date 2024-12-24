package com.bot.pets_bot.exeptions;

public class BadPage extends RuntimeException {
    public BadPage(String message) {
        super(message);
    }
}
