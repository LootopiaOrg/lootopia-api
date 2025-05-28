package com.lootopiaApi.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.lootopiaApi.model.entity.Hunt;
import com.lootopiaApi.model.entity.Participation;
import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.repository.HuntRepository;
import com.lootopiaApi.repository.ParticipationRepository;
import com.lootopiaApi.repository.UserRepository;
import com.lootopiaApi.service.ParticipationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final HuntRepository huntRepository;
    private final UserRepository userRepository;

    public ParticipationServiceImpl(
            ParticipationRepository participationRepository,
            HuntRepository huntRepository,
            UserRepository userRepository
    ) {
        this.participationRepository = participationRepository;
        this.huntRepository = huntRepository;
        this.userRepository = userRepository;
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
            return existing.get();
        }

        Hunt hunt = huntRepository.findById(huntId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chasse non trouvée."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé."));

        /*

        // Déduire les couronnes si un frais de participation est défini
        Integer fee = hunt.getParticipationFee() != null ? hunt.getParticipationFee() : 0;
        if (user.getCrowns() < fee) {
            throw new IllegalStateException("Solde de couronnes insuffisant.");
        }

        user.setCrowns(user.getCrowns() - fee);
        userRepository.save(user);
        */

        Participation participation = new Participation();
        participation.setPlayer(user);
        participation.setHunt(hunt);
        participation.setCurrentStepNumber(0);
        participation.setCompleted(false);

        return participationRepository.save(participation);
    }
}
