package com.lootopiaApi.mappers;

import com.lootopiaApi.DTOs.HuntDto;
import com.lootopiaApi.DTOs.HuntStepDto;
import com.lootopiaApi.model.entity.Hunt;
import com.lootopiaApi.model.entity.HuntStep;
import com.lootopiaApi.model.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class HuntMapper {

    public static Hunt toEntity(HuntDto dto, User partner) {
        Hunt hunt = new Hunt();
        hunt.setTitle(dto.getTitle());
        hunt.setDescription(dto.getDescription());
        hunt.setLocation(dto.getLocation());
        hunt.setStartDate(dto.getStartDate());
        hunt.setEndDate(dto.getEndDate());
        hunt.setLevel(dto.getLevel());
        hunt.setImage(dto.getImage());
        hunt.setPartnerId(partner);

        List<HuntStep> steps = dto.getSteps().stream()
                .map(stepDto -> HuntStepMapper.toEntity(stepDto, hunt))
                .collect(Collectors.toList());

        hunt.setSteps(steps);
        return hunt;
    }

    public static HuntDto toDto(Hunt hunt) {
        HuntDto dto = new HuntDto();
        dto.setTitle(hunt.getTitle());
        dto.setDescription(hunt.getDescription());
        dto.setLocation(hunt.getLocation());
        dto.setStartDate(hunt.getStartDate());
        dto.setEndDate(hunt.getEndDate());
        dto.setLevel(hunt.getLevel());
        dto.setImage(hunt.getImage());

        List<HuntStepDto> stepDtos = hunt.getSteps().stream()
                .map(HuntStepMapper::toDto)
                .collect(Collectors.toList());

        dto.setSteps(stepDtos);
        return dto;
    }
}
