package com.lootopiaApi.service.impl;

import com.lootopiaApi.model.entity.UserBalance;
import com.lootopiaApi.repository.UserBalanceRepository;
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
        participation.setCurrentStepNumber(0);
        participation.setCompleted(false);

        return participationRepository.save(participation);
    }
}
