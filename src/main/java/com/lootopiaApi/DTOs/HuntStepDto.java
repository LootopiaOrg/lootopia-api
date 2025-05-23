package com.lootopiaApi.DTOs;

import lombok.Data;

@Data
public class HuntStepDto {
    private int stepNumber;
    private String riddle;
    private String hint; // optionnel
    private String type; // à valider (ex: "enigme", "repere")
    private String validationKey; // à valider (ex: "passphrase", "repere", "cache")
    private Double latitude; // optionnel
    private Double longitude; // optionnel
    private String illustration; // optionnel
}
