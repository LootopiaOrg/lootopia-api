package com.lootopiaApi.controller;

import com.lootopiaApi.DTOs.HuntDto;
import com.lootopiaApi.mappers.HuntMapper;
import com.lootopiaApi.model.entity.Hunt;
import com.lootopiaApi.service.HuntService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/partners/hunts")
@PreAuthorize("hasRole('PARTNER')")
public class PartnerHuntController {

    private final HuntService huntService;

    public PartnerHuntController(HuntService huntService) {
        this.huntService = huntService;
    }

    @PostMapping
    public ResponseEntity<Hunt> create(@RequestBody HuntDto hunt) throws AccessDeniedException {
        return ResponseEntity.ok(huntService.createHunt(hunt));
    }

    @GetMapping
    public ResponseEntity<Page<HuntDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Hunt> huntPage = huntService.findAll(PageRequest.of(page, size));
        Page<HuntDto> dtoPage = huntPage.map(HuntMapper::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HuntDto> getById(@PathVariable Long id) {
        Hunt hunt = huntService.findById(id); // orElseThrow inside service
        HuntDto dto = HuntMapper.toDto(hunt);
        return ResponseEntity.ok(dto);
    }

}
