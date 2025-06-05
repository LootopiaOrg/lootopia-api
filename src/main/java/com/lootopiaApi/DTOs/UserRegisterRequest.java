package com.lootopiaApi.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String firstName;
    private String lastName;
    private String bio;
    private boolean partner;
}

