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

@RestController
@RequestMapping("/api/partners/hunts")
@PreAuthorize("hasRole('PARTNER') or hasRole('ADMIN')")
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
        Hunt hunt = huntService.findById(id);
        HuntDto dto = HuntMapper.toDto(hunt);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HuntDto> updateHunt(@PathVariable Long id, @RequestBody HuntDto dto) throws AccessDeniedException {
        Hunt updatedHunt = huntService.updateHunt(id, dto);
        return ResponseEntity.ok(HuntMapper.toDto(updatedHunt));
    }

    @DeleteMapping("/{huntId}")
    public ResponseEntity<Void> deleteHunt(@PathVariable Long huntId) {
        huntService.deleteHunt(huntId);
        return ResponseEntity.noContent().build();
    }

}