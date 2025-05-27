package com.lootopiaApi.controller;

import com.lootopiaApi.DTOs.*;
import com.lootopiaApi.model.entity.Address;
import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.service.AddressService;
import com.lootopiaApi.service.ProfileImagesService;
import com.lootopiaApi.service.UserBalanceService;
import com.lootopiaApi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AddressService addressService;
    private final ProfileImagesService profileImagesService;
    private final UserBalanceService userBalanceService;


    @PostMapping("/upload/profile")
    public ResponseEntity<ApiResponse> uploadProfileImage(@RequestBody String base64Image) {
        User user = userService.getAuthenticatedUser();
        profileImagesService.uploadProfileImage(user, base64Image);
        return ResponseEntity.ok(new ApiResponse("Image uploaded successfully", "success"));
    }


    @GetMapping("/image/profile")
    public ResponseEntity<ApiResponse> getProfileImage() {
        User user = userService.getAuthenticatedUser();
        String base64 = profileImagesService.getProfileImage(user);

        if (base64 == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Aucune image trouvée", "error"));
        }

        return ResponseEntity.ok(new ApiResponse(base64, "success"));
    }


    @PostMapping("/upload/cover")
    public ResponseEntity<ApiResponse> uploadCoverImage(@RequestBody String base64Image) {
        User user = userService.getAuthenticatedUser();
        profileImagesService.uploadCoverImage(user, base64Image);
        return ResponseEntity.ok(new ApiResponse("Image de couverture envoyée avec succès", "success"));
    }


    @GetMapping("/image/cover")
    public ResponseEntity<ApiResponse> getCoverImage() {
        User user = userService.getAuthenticatedUser();
        String base64 = profileImagesService.getCoverImage(user);

        if (base64 == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Aucune image trouvée", "error"));
        }

        return ResponseEntity.ok(new ApiResponse(base64, "success"));
    }

    @GetMapping("/balance")
    public ResponseEntity<Integer> getBalance() {
        User user = userService.getAuthenticatedUser();
        return ResponseEntity.ok(this.userBalanceService.getCrowns(user.getId()));
    }

    @GetMapping
    public ResponseEntity<UserDto> getConnectedUser() {
        User user = userService.getAuthenticatedUser();
        UserDto dto = new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio()
        );
        return ResponseEntity.ok(dto);
    }

    @PostMapping()
    public ResponseEntity<UserDto> updateUserProfile(@RequestBody UserUpdateDTO request) {
        User user = userService.getAuthenticatedUser();
        userService.updateUserProfile(user, request);
        User updatedUser = userService.getAuthenticatedUser();

        UserDto dto = new UserDto(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getBio()
        );

        return ResponseEntity.ok(dto);
    }


    @PostMapping("/address")
    public ResponseEntity<Address> addAddress(@RequestBody AddressDto dto) {
        Address address = addressService.addAddressToAuthenticatedUser(dto);
        return ResponseEntity.ok(address);
    }


    @GetMapping("/address")
    public ResponseEntity<AddressDto> getAddressOfAuthenticatedUser() {
        return addressService.getAddressDtoOfAuthenticatedUser()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mfa-info")
    public ResponseEntity<MfaInfoDto> getMfaInfo() {
        User user = userService.getAuthenticatedUser();
        MfaInfoDto mfaInfo = new MfaInfoDto(user.isMfaEnabled(), user.getSecretKey());
        return ResponseEntity.ok(mfaInfo);
    }


    @PostMapping("/mfa/toggle")
    public ResponseEntity<ApiResponse> toggleMfa() {
        User user = userService.getAuthenticatedUser();
        ApiResponse response = userService.toggleMfa(user);

        return ResponseEntity.ok(response);
    }

}
