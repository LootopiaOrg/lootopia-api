package com.lootopiaApi.DTOs;

import com.lootopiaApi.model.enums.HuntLevel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HuntPlayerDto {
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private HuntLevel level;
    private String image;
    private String mode;
    private Integer maxParticipants;
    private Integer participationFee;
    private Integer digDelaySeconds;
    private String organizerNickname;
}
