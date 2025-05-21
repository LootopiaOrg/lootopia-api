package com.lootopiaApi.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDto {
    private String street;
    private String city;
    private String zipCode;
    private String country;
}