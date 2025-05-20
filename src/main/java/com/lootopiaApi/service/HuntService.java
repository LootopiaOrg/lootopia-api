package com.lootopiaApi.service;

import com.lootopiaApi.DTOs.HuntDto;
import com.lootopiaApi.model.entity.Hunt;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface HuntService {

    Hunt createHunt(HuntDto hunt) throws AccessDeniedException;
    List<Hunt> findByPartnerId();
}
