package com.lootopiaApi.mappers;

import com.lootopiaApi.DTOs.*;
import com.lootopiaApi.model.entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.lootopiaApi.model.enums.HuntLevel;

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
        hunt.setMode(dto.getMode());
        hunt.setAccessMode(dto.getAccessMode());
        hunt.setChatEnabled(dto.getChatEnabled());
        hunt.setMaxParticipants(dto.getMaxParticipants());
        hunt.setParticipationFee(dto.getParticipationFee());
        hunt.setDigDelaySeconds(dto.getDigDelaySeconds());
        hunt.setOrganizer(partner);
        List<HuntStep> steps = dto.getSteps().stream()
                .map(stepDto -> HuntStepMapper.toEntity(stepDto, hunt))
                .collect(Collectors.toList());
        hunt.setSteps(steps);
        List<MapConfig> mapEntities = dto.getMaps().stream()
                .map(mapDto -> MapMapper.toEntity(mapDto, hunt))
                .collect(Collectors.toList());
        hunt.setMaps(mapEntities);

        List<Reward> rewards = dto.getRewards().stream()
                .map(rewardDto -> RewardMapper.toEntity(rewardDto, hunt))
                .collect(Collectors.toList());
        hunt.setRewards(rewards);

        return hunt;
    }

    public static HuntDto toDto(Hunt hunt) {
        HuntDto dto = new HuntDto();
        dto.setId(hunt.getId());
        dto.setTitle(hunt.getTitle());
        dto.setDescription(hunt.getDescription());
        dto.setLocation(hunt.getLocation());
        dto.setStartDate(hunt.getStartDate());
        dto.setEndDate(hunt.getEndDate());
        dto.setLevel(hunt.getLevel());
        dto.setImage(hunt.getImage());
        dto.setMode(hunt.getMode());
        dto.setAccessMode(hunt.getAccessMode());
        dto.setChatEnabled(hunt.getChatEnabled());
        dto.setMaxParticipants(hunt.getMaxParticipants());
        dto.setParticipationFee(hunt.getParticipationFee());
        dto.setDigDelaySeconds(hunt.getDigDelaySeconds());
        dto.setOrganizerNickname(hunt.getOrganizer().getFirstName());
        List<HuntStepDto> stepDtos = hunt.getSteps().stream()
                .map(HuntStepMapper::toDto)
                .collect(Collectors.toList());
        dto.setSteps(stepDtos);
        List<MapConfigDto> mapDtos = hunt.getMaps().stream()
                .map(MapMapper::toDto)
                .collect(Collectors.toList());
        dto.setMaps(mapDtos);
        return dto;
    }

    public static HuntPlayerDto toPlayerDto(Hunt hunt) {
        HuntPlayerDto dto = new HuntPlayerDto();
        dto.setId(hunt.getId());
        dto.setTitle(hunt.getTitle());
        dto.setDescription(hunt.getDescription());
        dto.setLocation(hunt.getLocation());
        dto.setStartDate(hunt.getStartDate());
        dto.setEndDate(hunt.getEndDate());
        dto.setLevel(hunt.getLevel());
        dto.setImage(hunt.getImage());
        dto.setMode(hunt.getMode());
        dto.setMaxParticipants(hunt.getMaxParticipants());
        dto.setParticipationFee(hunt.getParticipationFee());
        dto.setDigDelaySeconds(hunt.getDigDelaySeconds());
        dto.setOrganizerNickname(hunt.getOrganizer().getUsername());

        List<Reward> rewards = hunt.getRewards();
        if (rewards != null && !rewards.isEmpty()) {
            Reward firstReward = rewards.get(0);
            if ("couronne".equalsIgnoreCase(firstReward.getType())) {
                dto.setReward(firstReward.getValue() + " couronnes");
            } else {
                dto.setReward("Récompense externe");
            }
        } else {
            dto.setReward("Aucune récompense");
        }
        return dto;
    }

    public static HuntParticipantDto toParticipantDto(Participation participation) {
        Hunt hunt = participation.getHunt();

        HuntParticipantDto dto = new HuntParticipantDto();
        dto.setId(hunt.getId());
        dto.setTitle(hunt.getTitle());
        dto.setDescription(hunt.getDescription());
        dto.setLocation(hunt.getLocation());
        dto.setStartDate(hunt.getStartDate());
        dto.setEndDate(hunt.getEndDate());
        dto.setLevel(hunt.getLevel());
        dto.setImage(hunt.getImage());
        dto.setMode(hunt.getMode());
        dto.setMaxParticipants(hunt.getMaxParticipants());
        dto.setParticipationFee(hunt.getParticipationFee());
        dto.setDigDelaySeconds(hunt.getDigDelaySeconds());
        dto.setOrganizerNickname(hunt.getOrganizer().getUsername());
        dto.setParticipationId(participation.getId());

        // Add participant progression
        dto.setCurrentStepNumber(participation.getCurrentStepNumber());
        dto.setCompletedStepIds(participation.getCompletedStepIds());
        dto.setCompleted(participation.isCompleted());
        dto.setJoinedAt(participation.getJoinedAt());
        dto.setCompletedAt(participation.getCompletedAt());

        // Steps
        List<HuntStepPlayerDto> stepDtos = hunt.getSteps().stream()
                .map(HuntStepMapper::toPlayerDto)
                .collect(Collectors.toList());
        dto.setSteps(stepDtos);

        // Maps
        List<MapConfigDto> mapDtos = hunt.getMaps().stream()
                .map(MapMapper::toDto)
                .collect(Collectors.toList());
        dto.setMaps(mapDtos);

        return dto;
    }

}
