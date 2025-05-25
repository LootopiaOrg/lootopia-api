package com.lootopiaApi.service;

import com.lootopiaApi.model.entity.Offer;

import java.util.List;

public interface OfferService {

    List<Offer> findActiveOffers();
}
