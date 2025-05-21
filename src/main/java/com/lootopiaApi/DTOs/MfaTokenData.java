package com.lootopiaApi.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MfaTokenData {
    private String qrCode;
    private String mfaCode;
}
