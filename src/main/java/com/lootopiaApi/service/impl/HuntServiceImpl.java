package com.lootopiaApi.service.impl;

import com.lootopiaApi.DTOs.HuntDto;
import com.lootopiaApi.mappers.HuntMapper;
import com.lootopiaApi.model.entity.Hunt;
import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.repository.HuntRepository;
import com.lootopiaApi.service.HuntService;
import com.lootopiaApi.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class HuntServiceImpl implements HuntService {

    private final HuntRepository huntRepository;
    private final UserService userService;

    public HuntServiceImpl(HuntRepository huntRepository, UserService userService) {
        this.huntRepository = huntRepository;
        this.userService = userService;
    }

    @Override
    public Hunt createHunt(HuntDto dto) throws AccessDeniedException {
        this.userService.assertIsPartner();
        User partner = this.userService.getAuthenticatedUser();

        Hunt hunt = HuntMapper.toEntity(dto, partner);
        return huntRepository.save(hunt);
    }

    @Override
    public List<Hunt> findByPartnerId() {
        User user = this.userService.getAuthenticatedUser();
        return this.huntRepository.findByPartnerId_Id(user.getId());
    }

    @Override
    public Page<Hunt> findAll(Pageable pageable) {
        return huntRepository.findAll(pageable);
    }

    @Override
    public Hunt findById(Long id) {
        return huntRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chasse non trouvée"));
    }

}
