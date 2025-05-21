package com.lootopiaApi.service.impl;

import com.lootopiaApi.model.entity.ProfileImages;
import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.repository.ProfileImagesRepository;
import com.lootopiaApi.service.ProfileImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileImagesServiceImpl implements ProfileImagesService {

    private final ProfileImagesRepository profileImagesRepository;

    @Override
    public void uploadProfileImage(User user, String base64Image) {
        ProfileImages image = profileImagesRepository.findByUser(user)
                .orElseGet(() -> ProfileImages.builder().user(user).build());

        image.setProfileImage(base64Image);
        profileImagesRepository.save(image);
    }


    @Override
    public String getProfileImage(User user) {
        return profileImagesRepository.findByUser(user)
                .map(ProfileImages::getProfileImage)
                .orElse(null);
    }

    @Override
    public void uploadCoverImage(User user, String base64Image) {
        ProfileImages image = profileImagesRepository.findByUser(user)
                .orElseGet(() -> ProfileImages.builder().user(user).build());

        image.setCoverImage(base64Image);
        profileImagesRepository.save(image);
    }

    @Override
    public String getCoverImage(User user) {
        return profileImagesRepository.findByUser(user)
                .map(ProfileImages::getCoverImage)
                .orElse(null);
    }

}
