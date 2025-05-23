package com.lootopiaApi.DTOs;

import lombok.Data;

@Data
public class RewardDto {
    private String type; // ENUM strict: COURONNE, ARTEFACT, OBJET
    private String name;     // facultatif sauf pour ARTEFACT/OBJET
    private Integer value;   // facultatif sauf pour COURONNE
}
