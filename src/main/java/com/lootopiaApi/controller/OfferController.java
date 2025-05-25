package com.lootopiaApi.controller;

import com.lootopiaApi.model.entity.Offer;
import com.lootopiaApi.service.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/offer")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public ResponseEntity<List<Offer>> findActiveOffers() {
        return ResponseEntity.ok(offerService.findActiveOffers());
    }
}
