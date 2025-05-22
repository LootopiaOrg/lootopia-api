package com.lootopiaApi.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "steps")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HuntStep extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int stepNumber;

    @Column(columnDefinition = "TEXT")
    private String riddle;

    private String hint;

    private String type; // exemple : "énigme", "repère", "cache".

    private String validationKey; // exemple : "passphrase", "repere", "cache"

    private Double latitude;

    private Double longitude;

    @Column(columnDefinition = "TEXT")
    private String illustration;

    @ManyToOne
    @JoinColumn(name = "hunt_id")
    @JsonBackReference
    private Hunt hunt;
}
