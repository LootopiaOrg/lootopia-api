package com.lootopiaApi.controller;

import com.lootopiaApi.DTOs.*;
import com.lootopiaApi.mappers.HuntMapper;
import com.lootopiaApi.model.entity.Hunt;
import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.service.HuntService;
import com.lootopiaApi.service.ParticipationService;
import com.lootopiaApi.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.lootopiaApi.model.entity.Participation;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/hunts")
@PreAuthorize("hasRole('USER')")
public class UserHuntController {

    private final HuntService huntService;
    private final ParticipationService participationService;
    private final UserService userService;


    public UserHuntController(HuntService huntService, ParticipationService participationService, UserService userService) {
        this.huntService = huntService;
        this.participationService = participationService;
        this.userService = userService;
    }

    // GET all public hunts
    @GetMapping
    public ResponseEntity<Page<HuntPlayerDto>> listAvailableHunts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Hunt> hunts = huntService.findAllPublicAvailable(PageRequest.of(page, size));
        Page<HuntPlayerDto> dtos = hunts.map(HuntMapper::toPlayerDto);
        return ResponseEntity.ok(dtos);
    }

    // GET hunt by ID
    @GetMapping("/{id}")
    public ResponseEntity<HuntPlayerDto> getHuntDetails(@PathVariable Long id) {
        Hunt hunt = huntService.findById(id);
        return ResponseEntity.ok(HuntMapper.toPlayerDto(hunt));
    }

    // POST participate in hunt
    @PostMapping("/{id}/participate")
    public ResponseEntity<Participation> participate(@PathVariable Long id) {
        try {
            User user = userService.getAuthenticatedUser();
            Participation participation = participationService.participate(user.getId(), id);
            return ResponseEntity.ok(participation);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur de participation", e);
        }
    }

    // GET list of hunts user has participated in
    @GetMapping("/my-participations")
    public ResponseEntity<List<HuntParticipantDto>> getMyParticipatedHunts() {
        User user = userService.getAuthenticatedUser();
        List<Participation> participations = participationService.getUserParticipations(user.getId());

        List<HuntParticipantDto> hunts = participations.stream()
                .map(HuntMapper::toParticipantDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(hunts);
    }


    @PostMapping("/validate")
    public ResponseEntity<Map<String, String>> validateStep(@RequestBody StepValidationRequest request) {
        boolean success = participationService.validateStep(request);
        return ResponseEntity.ok(Map.of("message", "Étape validée !"));

    }

}