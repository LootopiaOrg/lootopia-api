package com.lootopiaApi.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rewards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // "couronne", "artefact", "objet"

    private String name;

    private Integer value; // si applicable (ex: nombre de couronnes)

    @ManyToOne
    @JoinColumn(name = "hunt_id")
    @JsonBackReference
    private Hunt hunt;
}
