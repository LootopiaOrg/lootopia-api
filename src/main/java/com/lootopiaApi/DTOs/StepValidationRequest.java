package com.lootopiaApi.DTOs;

import lombok.Data;

@Data
public class StepValidationRequest {
    private Long participationId;
    private int stepNumber;
    private String answer; // pour les énigmes
    private Double latitude;  // pour repère / cache
    private Double longitude;
}

