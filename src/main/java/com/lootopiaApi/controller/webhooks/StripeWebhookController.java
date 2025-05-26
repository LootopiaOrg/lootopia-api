package com.lootopiaApi.controller.webhooks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lootopiaApi.service.StripeService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionRetrieveParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/stripe/webhook")
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String secretKey;

    private final StripeService stripeService;

    public StripeWebhookController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping
    public ResponseEntity<String> handleStripeEvent(
        @RequestBody String payload,
        @RequestHeader("Stripe-Signature") String sigHeader
    ) throws StripeException {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, secretKey);
            log.info("📦 Payload brut reçu : {}", payload);
        } catch (SignatureVerificationException e) {
            log.error("Signature invalide : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        log.info("Stripe event reçu : {}", event.getType());

        if ("checkout.session.completed".equals(event.getType())) {
            String sessionId = null;

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(payload);
                sessionId = root.path("data").path("object").path("id").asText();

                log.info("sessionId reçu : {}", sessionId);

                Session fullSession = Session.retrieve(
                        sessionId,
                        SessionRetrieveParams.builder()
                                .addExpand("line_items")
                                .build(),
                        null
                );

                log.info("Session rechargée : {}", fullSession.getId());
                stripeService.creditUserFromCheckoutSession(fullSession);

            } catch (Exception e) {
                log.error("Erreur lors du traitement du webhook Stripe : ", e);
            }
        }

        return ResponseEntity.ok("Event processed successfully");
    }
}
