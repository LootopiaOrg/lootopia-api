package com.lootopiaApi.repository;

import com.lootopiaApi.model.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {
    EmailConfirmationToken findByToken(String token);
}
