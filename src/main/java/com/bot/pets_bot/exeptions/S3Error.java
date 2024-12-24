package com.bot.pets_bot.exeptions;

public class S3Error extends RuntimeException {
    public S3Error(String message) {
        super(message);
    }
}
