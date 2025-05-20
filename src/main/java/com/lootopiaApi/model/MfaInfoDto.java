package com.lootopiaApi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MfaInfoDto {
    private boolean mfaEnabled;
    private String secretKey;
}