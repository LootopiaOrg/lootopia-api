package com.lootopiaApi.controller;

import com.lootopiaApi.DTOs.HuntDto;
import com.lootopiaApi.DTOs.UserDto;
import com.lootopiaApi.mappers.HuntMapper;
import com.lootopiaApi.model.entity.Hunt;
import com.lootopiaApi.service.AdminUserService;
import com.lootopiaApi.service.HuntService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final HuntService huntService;
    private final AdminUserService adminUserService;

    public AdminController(HuntService huntService, AdminUserService adminUserService) {
        this.huntService = huntService;
        this.adminUserService = adminUserService;
    }

    @GetMapping("/hunts")
    public ResponseEntity<Page<HuntDto>> getAllHunts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Hunt> huntPage = huntService.findAll(PageRequest.of(page, size));
        Page<HuntDto> dtoPage = huntPage.map(HuntMapper::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserDto>> searchUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String role
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDto> result = adminUserService.searchUsers(query, role, pageable);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/users/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        adminUserService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }
}
