package com.lootopiaApi.controller;

import com.lootopiaApi.DTOs.HuntDto;
import com.lootopiaApi.mappers.HuntMapper;
import com.lootopiaApi.model.entity.Hunt;
import com.lootopiaApi.service.HuntService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/hunts")
@PreAuthorize("hasRole('ADMIN')")
public class AdminHuntController {

    private final HuntService huntService;

    public AdminHuntController(HuntService huntService) {
        this.huntService = huntService;
    }

    @GetMapping
    public ResponseEntity<Page<HuntDto>> getAllHunts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Hunt> huntPage = huntService.findAll(PageRequest.of(page, size));
        Page<HuntDto> dtoPage = huntPage.map(HuntMapper::toDto);
        return ResponseEntity.ok(dtoPage);
    }
}
