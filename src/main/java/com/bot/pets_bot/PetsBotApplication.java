package com.bot.pets_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class PetsBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetsBotApplication.class, args);
	}

}
