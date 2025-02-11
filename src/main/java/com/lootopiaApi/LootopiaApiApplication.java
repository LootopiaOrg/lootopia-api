package com.lootopiaApi;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class LootopiaApiApplication {


	public static void main(String[] args) {
		SpringApplication.run(LootopiaApiApplication.class, args);
	}

}
