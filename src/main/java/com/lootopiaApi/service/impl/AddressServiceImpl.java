package com.lootopiaApi.service.impl;

import com.lootopiaApi.DTOs.AddressDto;
import com.lootopiaApi.model.entity.Address;
import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.repository.AddressRepository;
import com.lootopiaApi.service.AddressService;
import com.lootopiaApi.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;

    public AddressServiceImpl(AddressRepository addressRepository, UserService userService) {
        this.addressRepository = addressRepository;
        this.userService = userService;
    }

    public Address addAddressToAuthenticatedUser(AddressDto dto) {
        User user = userService.getAuthenticatedUser();

        Address address = user.getAddress();

        if (address == null) {
            address = new Address();
            address.setUser(user);
        }

        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setZipCode(dto.getZipCode());
        address.setCountry(dto.getCountry());

        user.setAddress(address);

        return addressRepository.save(address);
    }

    public Optional<AddressDto> getAddressDtoOfAuthenticatedUser() {
        User user = userService.getAuthenticatedUser();
        Address address = user.getAddress();

        if (address == null) {
            return Optional.empty();
        }

        AddressDto dto = AddressDto.builder()
                .street(address.getStreet())
                .city(address.getCity())
                .zipCode(address.getZipCode())
                .country(address.getCountry())
                .build();

        return Optional.of(dto);
    }


}
