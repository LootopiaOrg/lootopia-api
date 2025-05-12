package com.lootopiaApi.service.impl;

import com.lootopiaApi.model.entity.Hunt;
import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.repository.HuntRepository;
import com.lootopiaApi.service.HuntService;
import com.lootopiaApi.service.UserService;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class HuntServiceImpl implements HuntService {

    private final HuntRepository huntRepository;
    private final UserService userService;

    public HuntServiceImpl(HuntRepository huntRepository, UserService userService) {
        this.huntRepository = huntRepository;
        this.userService = userService;
    }

    @Override
    public Hunt createHunt(Hunt hunt) throws AccessDeniedException {
        this.userService.assertIsPartner();
        hunt.setCreator(this.userService.getAuthenticatedUser());
        return this.huntRepository.save(hunt);
    }

    @Override
    public List<Hunt> findByCreatorId() {
        User user = this.userService.getAuthenticatedUser();
        return this.huntRepository.findByCreatorId(user.getId());
    }
}
