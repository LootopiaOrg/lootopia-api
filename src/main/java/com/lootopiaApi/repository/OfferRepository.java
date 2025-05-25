package com.lootopiaApi.repository;

import com.lootopiaApi.model.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByActive(boolean active);
}
