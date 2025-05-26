package com.lootopiaApi.service;

import com.lootopiaApi.model.entity.Offer;
import com.lootopiaApi.repository.OfferRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class StripeService {
    @Value("${stripe.secret.key}")
    private String secretKey;

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    private final OfferRepository offerRepository;

    private final UserBalanceService userBalanceService;

    public StripeService(OfferRepository offerRepository, UserBalanceService userBalanceService) {
        this.offerRepository = offerRepository;
        this.userBalanceService = userBalanceService;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public String createCheckoutSession(String stripePriceId, Long userId) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .setClientReferenceId(userId.toString())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPrice(stripePriceId)
                                .build()
                )
                .build();

        Session session = Session.create(params);
        log.info("Stripe Checkout URL: {}", session.getUrl());
        return session.getUrl();
    }

    public void creditUserFromCheckoutSession(Session session) {
        try {
            String userIdStr = session.getClientReferenceId();
            if (userIdStr == null) {
                log.warn("Aucun clientReferenceId trouvé dans la session.");
                return;
            }

            Long userId = Long.parseLong(userIdStr);

            String priceId = session.getLineItems().getData().get(0).getPrice().getId();

            Offer offer = offerRepository.stripePriceId(priceId)
                    .orElseThrow(() -> new IllegalStateException("Offre non trouvée pour priceId: " + priceId));

            userBalanceService.creditCrowns(userId, offer.getCrowns());

            log.info("Crédité {} couronnes à l'utilisateur {}", offer.getCrowns(), userId);

        } catch (Exception e) {
            log.error("Erreur lors du traitement du webhook Stripe", e);
        }
    }
}
