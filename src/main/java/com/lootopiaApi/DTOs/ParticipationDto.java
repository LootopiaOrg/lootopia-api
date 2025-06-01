package com.lootopiaApi.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationDto {
    private Long participationId;
    private HuntParticipantDto hunt;
    private int currentStepNumber;
    private Set<Long> completedStepIds;
    private boolean completed;
    private LocalDateTime joinedAt;
    private LocalDateTime completedAt;
}

