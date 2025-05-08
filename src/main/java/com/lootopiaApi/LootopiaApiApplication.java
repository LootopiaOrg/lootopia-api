package com.lootopiaApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LootopiaApiApplication {


	public static void main(String[] args) {
		SpringApplication.run(LootopiaApiApplication.class, args);
	}

}
