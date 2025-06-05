package com.lootopiaApi.repository;

import com.lootopiaApi.model.entity.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {
    EmailConfirmationToken findByToken(String token);
    void deleteByUserIdIn(List<Long> userIds);
}
