package com.lootopiaApi.config;

import com.lootopiaApi.model.entity.Offer;
import com.lootopiaApi.model.entity.Role;
import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.model.enums.ERole;
import com.lootopiaApi.repository.OfferRepository;
import com.lootopiaApi.repository.RoleRepository;
import com.lootopiaApi.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
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
                    Offer.builder().crowns(100).price(1.99).description("Découverte express").stripePriceId("price_1RSSMx4JtcL5tx0DQgw8zoJ6").active(true).build(),
                    Offer.builder().crowns(250).price(4.99).description("Pack parfait pour débuter").stripePriceId("price_1RSSNn4JtcL5tx0DD0ZgDMsz").active(true).build(),
                    Offer.builder().crowns(500).price(8.99).description("Le plus populaire").badge("popular").stripePriceId("price_1RSSOX4JtcL5tx0D3MBnOKGt").active(true).build(),
                    Offer.builder().crowns(1000).price(15.99).description("Offre spéciale premium").stripePriceId("price_1RSSP84JtcL5tx0D7Ot5TUd3").active(true).build(),
                    Offer.builder().crowns(2000).price(24.99).originalPrice(29.99).description("Pack des chasseurs pro").badge("promo").stripePriceId("price_1RSSQC4JtcL5tx0DZwKCBwAI").active(true).build(),
                    Offer.builder().crowns(5000).price(59.99).description("Le pack des légendes").stripePriceId("price_1RSSRA4JtcL5tx0DmfZu5akt").active(true).build()
            ));
            log.info("Offers initialized");
        };
    }

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminUsername = "admin";
            String adminPassword = "admin123";

            Role adminRole = roleRepository.findByRole(ERole.ADMIN)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setRole(ERole.ADMIN);
                        return roleRepository.save(newRole);
                    });

            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setFirstName("Admin");
                admin.setLastName("Lootopia");
                admin.setActive(true);
                admin.setAccountVerified(true);
                admin.setRoles(Collections.singleton(adminRole));
                userRepository.save(admin);
                log.info("Admin user created: {} / {}", adminUsername, adminPassword);
            }
        };
    }
}
