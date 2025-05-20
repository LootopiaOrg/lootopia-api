package com.lootopiaApi.DTOs;

import com.lootopiaApi.model.enums.HuntLevel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class HuntDto {
    private String title;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private HuntLevel level;
    private String image;
    private List<HuntStepDto> steps = new ArrayList<>();
}
