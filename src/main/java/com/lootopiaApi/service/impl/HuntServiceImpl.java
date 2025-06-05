package com.lootopiaApi.service.impl;

import com.lootopiaApi.DTOs.HuntDto;
import com.lootopiaApi.mappers.HuntMapper;
import com.lootopiaApi.model.entity.Hunt;
import com.lootopiaApi.model.entity.HuntStep;
import com.lootopiaApi.model.entity.Participation;
import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.model.enums.ERole;
import com.lootopiaApi.repository.HuntRepository;
import com.lootopiaApi.repository.ParticipationRepository;
import com.lootopiaApi.service.HuntService;
import com.lootopiaApi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HuntServiceImpl implements HuntService {

    private final HuntRepository huntRepository;
    private final UserService userService;
    private final ParticipationRepository participationRepository;

    public HuntServiceImpl(HuntRepository huntRepository, UserService userService, ParticipationRepository participationRepository) {
        this.huntRepository = huntRepository;
        this.userService = userService;
        this.participationRepository = participationRepository;
    }

    @Override
    public Hunt createHunt(HuntDto dto) throws AccessDeniedException {
        User partner = this.userService.getAuthenticatedUser();

        Hunt hunt = HuntMapper.toEntity(dto, partner);
        return huntRepository.save(hunt);
    }

    @Override
    public Page<Hunt> findByPartnerId(Pageable pageable) {
        User user = this.userService.getAuthenticatedUser();
        return this.huntRepository.findByOrganizer_Id(user.getId(), pageable);
    }

    @Override
    public Page<Hunt> findAllPublicAvailable(Pageable pageable) {
        return huntRepository.findByAccessModeAndEndDateAfter("public", LocalDateTime.now(), pageable);
    }

    @Override
    public Hunt updateHunt(Long id, HuntDto dto) throws AccessDeniedException {
        Hunt existingHunt = huntRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Hunt not found"));

        User currentUser = userService.getAuthenticatedUser();
        boolean isAdmin = currentUser.getRoles().stream().anyMatch(r -> r.getRole().equals(ERole.ADMIN));
        if (!isAdmin && !existingHunt.getOrganizer().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed to update this hunt.");
        }

        existingHunt.setTitle(dto.getTitle());
        existingHunt.setDescription(dto.getDescription());
        existingHunt.setLocation(dto.getLocation());
        existingHunt.setStartDate(dto.getStartDate());
        existingHunt.setEndDate(dto.getEndDate());
        existingHunt.setLevel(dto.getLevel());
        existingHunt.setMode(dto.getMode());
        existingHunt.setAccessMode(dto.getAccessMode());
        existingHunt.setChatEnabled(dto.getChatEnabled());
        existingHunt.setMaxParticipants(dto.getMaxParticipants());
        existingHunt.setParticipationFee(dto.getParticipationFee());
        existingHunt.setDigDelaySeconds(dto.getDigDelaySeconds());
        existingHunt.setImage(dto.getImage());

        existingHunt.getSteps().clear(); // Vide proprement la liste
        dto.getSteps().forEach(stepDto -> {
            HuntStep step = new HuntStep();
            step.setStepNumber(stepDto.getStepNumber());
            step.setRiddle(stepDto.getRiddle());
            step.setHint(stepDto.getHint());
            step.setType(stepDto.getType());
            step.setValidationKey(stepDto.getValidationKey());
            step.setLatitude(stepDto.getLatitude());
            step.setLongitude(stepDto.getLongitude());
            step.setIllustration(stepDto.getIllustration());
            step.setHunt(existingHunt); // important !
            existingHunt.getSteps().add(step);
        });

        return huntRepository.save(existingHunt);
    }

    @Override
    public Page<Hunt> findAll(Pageable pageable) {
        return huntRepository.findAll(pageable);
    }

    @Override
    public Hunt findById(Long id) {
        return huntRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chasse non trouvée"));
    }

    @Transactional
    @Override
    public void deleteHunt(Long huntId) {
        Hunt hunt = huntRepository.findById(huntId)
                .orElseThrow(() -> new EntityNotFoundException("Chasse introuvable"));

        participationRepository.deleteAllByHunt(hunt);

        huntRepository.delete(hunt);
    }

}
