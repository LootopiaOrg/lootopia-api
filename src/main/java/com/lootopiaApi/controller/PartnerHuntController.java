package com.lootopiaApi.controller;

import com.lootopiaApi.model.entity.Hunt;
import com.lootopiaApi.service.HuntService;
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
    public ResponseEntity<Hunt> create(@RequestBody Hunt hunt) throws AccessDeniedException {
        return ResponseEntity.ok(huntService.createHunt(hunt));
    }

    @GetMapping
    public ResponseEntity<List<Hunt>> getAll() {
        return ResponseEntity.ok(huntService.findByCreatorId());
    }

}
