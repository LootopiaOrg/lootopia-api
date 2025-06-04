package com.lootopiaApi.service.impl;

import com.lootopiaApi.DTOs.StepValidationRequest;
import com.lootopiaApi.model.entity.*;
import com.lootopiaApi.repository.UserBalanceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.lootopiaApi.repository.HuntRepository;
import com.lootopiaApi.repository.ParticipationRepository;
import com.lootopiaApi.repository.UserRepository;
import com.lootopiaApi.service.ParticipationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final HuntRepository huntRepository;
    private final UserRepository userRepository;
    private final UserBalanceRepository userBalanceRepository;

    public ParticipationServiceImpl(
            ParticipationRepository participationRepository,
            HuntRepository huntRepository,
            UserRepository userRepository, UserBalanceRepository userBalanceRepository
    ) {
        this.participationRepository = participationRepository;
        this.huntRepository = huntRepository;
        this.userRepository = userRepository;
        this.userBalanceRepository = userBalanceRepository;
    }

    @Override
    public Participation getParticipation(Long userId, Long huntId) {
        return participationRepository.findByPlayerIdAndHuntId(userId, huntId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participation non trouvée."));
    }

    @Override
    public List<Participation> getUserParticipations(Long playerId) {
        return participationRepository.findAllByPlayerId(playerId);
    }

    @Override
    @Transactional
    public Participation participate(Long userId, Long huntId) {
        Optional<Participation> existing = participationRepository.findByPlayerIdAndHuntId(userId, huntId);
        if (existing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vous participez déjà à cette chasse.");
        }

        Hunt hunt = huntRepository.findById(huntId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chasse non trouvée."));

        if (hunt.getOrganizer() != null && hunt.getOrganizer().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "L'organisateur ne peut pas participer à sa propre chasse.");
        }

        long participantCount = participationRepository.countByHuntId(huntId);
        if (hunt.getMaxParticipants() != null && participantCount >= hunt.getMaxParticipants()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le nombre maximum de participants a été atteint.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé."));

        Optional<UserBalance> userBalanceOpt = userBalanceRepository.findByUserId(user.getId());

        UserBalance userBalance = userBalanceOpt.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Le solde utilisateur est introuvable.")
        );

        // Déduire les couronnes si un frais de participation est défini
        Integer fee = hunt.getParticipationFee() != null ? hunt.getParticipationFee() : 0;
        if (userBalance.getCrowns() < fee) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solde de couronnes insuffisant.");
        }

        userBalance.setCrowns(userBalance.getCrowns() - fee);
        userBalanceRepository.save(userBalance);

        Participation participation = new Participation();
        participation.setPlayer(user);
        participation.setHunt(hunt);
        participation.setCurrentStepNumber(1);
        participation.setCompleted(false);

        return participationRepository.save(participation);
    }

    public boolean validateStep(StepValidationRequest request) {

        Participation participation = participationRepository.findById(request.getParticipationId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participation introuvable"));

        Hunt hunt = participation.getHunt();

        // Rechercher l’étape correspondante par stepNumber
        Optional<HuntStep> optionalStep = hunt.getSteps().stream()
                .filter(step -> step.getStepNumber() == request.getStepNumber())
                .findFirst();

        if (optionalStep.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Étape non trouvée pour cette chasse");
        }

        HuntStep step = optionalStep.get();

        // Vérifier que c'est la bonne étape à valider
        if (step.getStepNumber() != participation.getCurrentStepNumber()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ce n'est pas l'étape attendue");
        }

        boolean isValid = switch (step.getType()) {
            case "enigme" -> {
                if (request.getAnswer() == null || request.getAnswer().isBlank()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Réponse manquante pour l'énigme");
                }
                yield step.getValidationKey().equalsIgnoreCase(request.getAnswer().trim());
            }
            case "repere", "cache" -> {
                if (request.getLatitude() == null || request.getLongitude() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coordonnées manquantes pour le repère/cache");
                }
                double distance = distanceInMeters(
                        step.getLatitude(), step.getLongitude(),
                        request.getLatitude(), request.getLongitude()
                );
                yield distance < 100.0;
            }
            default -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type d'étape inconnu");
            }
        };

        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validation de l'étape échouée");
        }

        // Marquer l'étape comme complétée
        participation.completeStep(step.getId());
        participation.setCurrentStepNumber(participation.getCurrentStepNumber());

        if (step.getStepNumber() == hunt.getSteps().size()) {
            participation.setCompleted(true);
            participation.setCompletedAt(LocalDateTime.now());
        }

        participationRepository.save(participation);

        return true;
    }



    private double distanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // Rayon de la terre
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

}
