package com.lootopiaApi.model;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String token;
    private String newPassword;
}
