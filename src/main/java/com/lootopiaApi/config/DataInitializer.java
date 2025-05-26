package com.lootopiaApi.config;

import com.lootopiaApi.model.entity.Offer;
import com.lootopiaApi.repository.OfferRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Log4j2
public class DataInitializer {

    @Bean
    CommandLineRunner initOffers(OfferRepository offerRepository) {
        return args -> {
            offerRepository.deleteAll();
            log.info("Offers deleted");
            offerRepository.saveAll(List.of(
                    Offer.builder().crowns(100).price(1.99).description("Découverte express").stripePriceId("prod_SNCmc7nWmYmTEU").active(true).build(),
                    Offer.builder().crowns(250).price(4.99).description("Pack parfait pour débuter").stripePriceId("prod_SNCnFPWacJXVsL").active(true).build(),
                    Offer.builder().crowns(500).price(8.99).description("Le plus populaire").badge("popular").stripePriceId("prod_SNCoRpJ4Jx4Kev").active(true).build(),
                    Offer.builder().crowns(1000).price(15.99).description("Offre spéciale premium").stripePriceId("prod_SNCoa9F41Rv9Er").active(true).build(),
                    Offer.builder().crowns(2000).price(24.99).originalPrice(29.99).description("Pack des chasseurs pro").badge("promo").stripePriceId("prod_SNCprZT3PFLh4w").active(true).build(),
                    Offer.builder().crowns(5000).price(59.99).description("Le pack des légendes").stripePriceId("prod_SNCqXcsJ2EBd7H").active(true).build()
            ));
            log.info("Offers initialized");
        };
    }
}
