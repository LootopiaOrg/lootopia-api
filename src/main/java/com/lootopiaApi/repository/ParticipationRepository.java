package com.lootopiaApi.repository;

import com.lootopiaApi.model.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    Optional<Participation> findByPlayerIdAndHuntId(Long playerId, Long huntId);
    List<Participation> findAllByPlayerId(Long playerId);
    long countByHuntId(Long huntId);
}
