package com.lootopiaApi.mappers;

import com.lootopiaApi.DTOs.MapConfigDto;
import com.lootopiaApi.model.entity.Hunt;
import com.lootopiaApi.model.entity.MapConfig;

public class MapMapper {

    public static MapConfig toEntity(MapConfigDto dto, Hunt hunt) {
        MapConfig map = new MapConfig();
        map.setName(dto.getName());
        map.setSkin(dto.getSkin());
        map.setZone(dto.getZone());
        map.setScaleMin(dto.getScaleMin());
        map.setScaleMax(dto.getScaleMax());
        map.setHunt(hunt);
        return map;
    }

    public static MapConfigDto toDto(MapConfig map) {
        MapConfigDto dto = new MapConfigDto();
        dto.setName(map.getName());
        dto.setSkin(map.getSkin());
        dto.setZone(map.getZone());
        dto.setScaleMin(map.getScaleMin());
        dto.setScaleMax(map.getScaleMax());
        return dto;
    }
}