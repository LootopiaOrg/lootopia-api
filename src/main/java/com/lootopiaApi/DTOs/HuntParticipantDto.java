package com.lootopiaApi.DTOs;

import com.lootopiaApi.model.enums.HuntLevel;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class HuntParticipantDto {
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private HuntLevel level;
    private String image;
    private String mode;
    private String accessMode;
    private Boolean chatEnabled;
    private Integer maxParticipants;
    private Integer participationFee;
    private Integer digDelaySeconds;
    private String organizerNickname;
    private List<HuntStepPlayerDto> steps = new ArrayList<>();
    private List<RewardDto> rewards = new ArrayList<>();
    private List<MapConfigDto> maps = new ArrayList<>();
}