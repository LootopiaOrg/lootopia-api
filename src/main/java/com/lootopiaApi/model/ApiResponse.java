package com.lootopiaApi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private String status; // You can use String or HttpStatus if you prefer
}