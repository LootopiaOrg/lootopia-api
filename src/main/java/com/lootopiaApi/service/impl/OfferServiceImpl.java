package com.lootopiaApi.service.impl;

import com.lootopiaApi.model.entity.Offer;
import com.lootopiaApi.repository.OfferRepository;
import com.lootopiaApi.service.OfferService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    public OfferServiceImpl(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    public List<Offer> findActiveOffers() {
        return offerRepository.findByActive(true);
    }
}
