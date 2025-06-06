package com.lootopiaApi.service;

import com.lootopiaApi.DTOs.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUserService {

    Page<UserDto> searchUsers(String query, String role, Boolean isActive, Pageable pageable);

    void deactivateUser(Long id);
}
