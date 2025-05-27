package com.lootopiaApi.service;

import com.lootopiaApi.model.entity.UserBalance;

public interface UserBalanceService {
    void creditCrowns(Long userId, int amount);
    UserBalance createBalanceFor(Long userId);
    int getCrowns(Long userId);
}
