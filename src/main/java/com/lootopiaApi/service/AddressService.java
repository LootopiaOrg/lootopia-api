package com.lootopiaApi.service;

import com.lootopiaApi.DTOs.AddressDto;
import com.lootopiaApi.model.entity.Address;

import java.util.Optional;

public interface AddressService {

    public Address addAddressToAuthenticatedUser(AddressDto dto);
    public Optional<AddressDto> getAddressDtoOfAuthenticatedUser();

}
