package com.lootopiaApi.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "participations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Participation extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private User player;

    @ManyToOne
    @JoinColumn(name = "hunt_id", nullable = false)
    private Hunt hunt;

    private LocalDateTime joinedAt = LocalDateTime.now();

    private boolean completed;

    private LocalDateTime completedAt;

    @ElementCollection
    @CollectionTable(name = "participation_completed_steps", joinColumns = @JoinColumn(name = "participation_id"))
    @Column(name = "step_id")
    private Set<Long> completedStepIds = new HashSet<>();

    private int currentStepNumber = 1;

    public void completeStep(Long stepId) {
        this.completedStepIds.add(stepId);
        this.currentStepNumber++;
    }

    public boolean hasCompletedStep(Long stepId) {
        return this.completedStepIds.contains(stepId);
    }
}
