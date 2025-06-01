package com.lootopiaApi.mappers;

import com.lootopiaApi.DTOs.HuntStepDto;
import com.lootopiaApi.DTOs.HuntStepPlayerDto;
import com.lootopiaApi.model.entity.Hunt;
import com.lootopiaApi.model.entity.HuntStep;

public class HuntStepMapper {

    public static HuntStep toEntity(HuntStepDto dto, Hunt hunt) {
        HuntStep step = new HuntStep();
        step.setStepNumber(dto.getStepNumber());
        step.setRiddle(dto.getRiddle());
        step.setHint(dto.getHint());
        step.setType(dto.getType());
        step.setValidationKey(dto.getValidationKey());
        step.setLatitude(dto.getLatitude());
        step.setLongitude(dto.getLongitude());
        step.setIllustration(dto.getIllustration());
        step.setHunt(hunt);
        return step;
    }

    public static HuntStepDto toDto(HuntStep step) {
        HuntStepDto dto = new HuntStepDto();
        dto.setStepNumber(step.getStepNumber());
        dto.setRiddle(step.getRiddle());
        dto.setHint(step.getHint());
        dto.setType(step.getType());
        dto.setValidationKey(step.getValidationKey());
        dto.setLatitude(step.getLatitude());
        dto.setLongitude(step.getLongitude());
        dto.setIllustration(step.getIllustration());
        return dto;
    }

    public static HuntStepPlayerDto toPlayerDto(HuntStep step) {
        HuntStepPlayerDto dto = new HuntStepPlayerDto();
        dto.setStepNumber(step.getStepNumber());
        dto.setRiddle(step.getRiddle());
        dto.setHint(step.getHint());
        dto.setType(step.getType());
        dto.setLatitude(step.getLatitude());
        dto.setLongitude(step.getLongitude());
        dto.setIllustration(step.getIllustration());
        return dto;
    }
}
