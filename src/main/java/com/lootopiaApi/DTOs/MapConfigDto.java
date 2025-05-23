package com.lootopiaApi.DTOs;

import lombok.Data;

@Data
public class MapConfigDto {
    private String name;
    private String skin;
    private String zone;
    private Integer scaleMin;
    private Integer scaleMax;
}