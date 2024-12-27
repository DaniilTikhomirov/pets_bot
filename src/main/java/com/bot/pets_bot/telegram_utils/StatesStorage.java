package com.bot.pets_bot.telegram_utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Setter
public class StatesStorage {
    private final Map<Long, Boolean> sendReport;

    public StatesStorage() {
        sendReport = new ConcurrentHashMap<>();
    }

}
