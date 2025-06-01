package com.lootopiaApi.service;

import com.lootopiaApi.DTOs.StepValidationRequest;
import com.lootopiaApi.model.entity.Participation;
import java.util.List;

public interface ParticipationService {

    public Participation getParticipation(Long userId, Long huntId);
    public Participation participate(Long userId, Long huntId);
    public List<Participation> getUserParticipations(Long userId);
    public boolean validateStep(StepValidationRequest request);
}
