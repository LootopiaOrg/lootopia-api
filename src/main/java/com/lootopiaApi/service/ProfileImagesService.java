package com.lootopiaApi.service;

import com.lootopiaApi.model.entity.User;


public interface ProfileImagesService {
    void uploadProfileImage(User user, String base64Image);
    String getProfileImage(User user);
    void uploadCoverImage(User user, String base64Image);
    String getCoverImage(User user);

}
