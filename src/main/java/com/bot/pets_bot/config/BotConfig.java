package com.bot.pets_bot.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * конфигурация бота имя и токен
 */
@Getter
@Configuration
@Data
public class BotConfig {


    @Value("${telegram.bot.name}")
    private String BotName;

    @Value("${telegram.bot.token}")
    private String BotToken;


}
