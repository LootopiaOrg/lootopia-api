package com.lootopiaApi.service.impl;

import com.lootopiaApi.DTOs.UserDto;
import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.model.enums.ERole;
import com.lootopiaApi.repository.UserRepository;
import com.lootopiaApi.service.AdminUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    public AdminUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserDto> searchUsers(String query, String role, Pageable pageable) {
        Page<User> users;

        if (query != null && role != null) {
            users = userRepository.findByQueryAndRole(query.toLowerCase(), ERole.valueOf(role.toUpperCase()), pageable);
        } else if (query != null) {
            users = userRepository.findByQuery(query.toLowerCase(), pageable);
        } else if (role != null) {
            users = userRepository.findByRole(ERole.valueOf(role.toUpperCase()), pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        return users.map(user -> new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio()
        ));
    }
}
