package com.lootopiaApi.service;

import com.lootopiaApi.DTOs.HuntDto;
import com.lootopiaApi.model.entity.Hunt;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface HuntService {

    Hunt createHunt(HuntDto hunt) throws AccessDeniedException;
    List<Hunt> findByPartnerId();
    Page<Hunt> findAll(Pageable pageable);

    Hunt findById(Long id);

    Page<Hunt> findAllPublicAvailable(Pageable pageable);

    Hunt updateHunt(Long id,HuntDto dto) throws AccessDeniedException;

    @Transactional
    void deleteHunt(Long huntId);
}
