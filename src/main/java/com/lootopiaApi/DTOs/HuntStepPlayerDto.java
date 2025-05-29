package com.lootopiaApi.DTOs;

import lombok.Data;

@Data
public class HuntStepPlayerDto {
    private int stepNumber;
    private String riddle;
    private String hint;
    private String type;
    private Double latitude;
    private Double longitude;
    private String illustration;
}
