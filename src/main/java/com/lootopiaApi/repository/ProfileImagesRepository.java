package com.lootopiaApi.repository;

import com.lootopiaApi.model.entity.ProfileImages;
import com.lootopiaApi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileImagesRepository extends JpaRepository<ProfileImages, Long> {
    Optional<ProfileImages> findByUser(User user);
    void deleteByUserIdIn(List<Long> userIds);
}
