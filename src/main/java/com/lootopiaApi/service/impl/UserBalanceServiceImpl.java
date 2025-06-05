package com.lootopiaApi.service.impl;

import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.model.entity.UserBalance;
import com.lootopiaApi.model.enums.ERole;
import com.lootopiaApi.repository.UserBalanceRepository;
import com.lootopiaApi.repository.UserRepository;
import com.lootopiaApi.service.UserBalanceService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserBalanceServiceImpl implements UserBalanceService {

    private final UserBalanceRepository userBalanceRepository;
    private final UserRepository userRepository;

    public UserBalanceServiceImpl(UserBalanceRepository userBalanceRepository, UserRepository userRepository) {
        this.userBalanceRepository = userBalanceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void creditCrowns(Long userId, int amount) {
        UserBalance balance = userBalanceRepository.findByUserId(userId)
                .orElseGet(() -> createBalanceFor(userId));

        balance.setCrowns(balance.getCrowns() + amount);
        userBalanceRepository.save(balance);
    }

    @Override
    public UserBalance createBalanceFor(Long userId) {
        Optional<User> user = this.userRepository.findById(userId);

        boolean isAdmin = user.get().getRoles().stream()
                .anyMatch(role -> role.getRole() == ERole.ADMIN);

        if (isAdmin) {
            return null;
        }

        return userBalanceRepository.save(
                UserBalance.builder().user(user.get()).crowns(50).build()
        );
    }

    @Override
    public int getCrowns(Long userId) {
        return userBalanceRepository.findByUserId(userId).get().getCrowns();
    }
}
