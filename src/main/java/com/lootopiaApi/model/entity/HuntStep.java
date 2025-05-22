package com.lootopiaApi.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private String location;

    @Column(columnDefinition = "TEXT")
    private String illustration;

    @ManyToOne
    @JoinColumn(name = "hunt_id")
    @JsonBackReference
    private Hunt hunt;
}
