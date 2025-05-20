package com.lootopiaApi.DTOs;

import lombok.Data;

@Data
public class HuntStepDto {
    private int stepNumber;
    private String riddle;
    private String hint;
    private String location;
    private String illustration;
}
