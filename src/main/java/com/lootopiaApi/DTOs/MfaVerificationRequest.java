package com.lootopiaApi.DTOs;

import lombok.Data;

@Data
public class MfaVerificationRequest {
    private String username;
    private String totp;
}
