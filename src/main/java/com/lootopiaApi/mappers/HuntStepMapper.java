package com.lootopiaApi.mappers;

import com.lootopiaApi.DTOs.HuntStepDto;
import com.lootopiaApi.model.entity.Hunt;
import com.lootopiaApi.model.entity.HuntStep;

public class HuntStepMapper {

    public static HuntStep toEntity(HuntStepDto dto, Hunt hunt) {
        HuntStep step = new HuntStep();
        step.setStepNumber(dto.getStepNumber());
        step.setRiddle(dto.getRiddle());
        step.setHint(dto.getHint());
        step.setLocation(dto.getLocation());
        step.setIllustration(dto.getIllustration());
        step.setHunt(hunt);
        return step;
    }

    public static HuntStepDto toDto(HuntStep step) {
        HuntStepDto dto = new HuntStepDto();
        dto.setStepNumber(step.getStepNumber());
        dto.setRiddle(step.getRiddle());
        dto.setHint(step.getHint());
        dto.setLocation(step.getLocation());
        dto.setIllustration(step.getIllustration());
        return dto;
    }
}
