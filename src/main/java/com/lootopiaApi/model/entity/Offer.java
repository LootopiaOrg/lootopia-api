package com.lootopiaApi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "offers")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Offer extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int crowns;
    private double price;
    private Double originalPrice;
    private String description;
    private String badge;

    @Column(name = "stripe_price_id", nullable = false)
    private String stripePriceId;

    @Column(nullable = false)
    private boolean active = true;
}
