package com.lootopiaApi.mappers;

import com.lootopiaApi.DTOs.RewardDto;
import com.lootopiaApi.model.entity.Hunt;
import com.lootopiaApi.model.entity.Reward;

public class RewardMapper {

    public static Reward toEntity(RewardDto dto, Hunt hunt) {
        Reward reward = new Reward();
        reward.setType(dto.getType());
        reward.setValue(dto.getValue());
        reward.setName(dto.getName());
        reward.setHunt(hunt);
        return reward;
    }

    public static RewardDto toDto(Reward reward) {
        RewardDto dto = new RewardDto();
        dto.setType(reward.getType());
        dto.setValue(reward.getValue());
        dto.setName(reward.getName());
        return dto;
    }
}
