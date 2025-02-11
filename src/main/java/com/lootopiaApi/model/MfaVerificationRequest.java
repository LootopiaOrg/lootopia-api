package com.lootopiaApi.model;

import lombok.Data;

@Data
public class MfaVerificationRequest {
    private String username;
    private String totp;
}
