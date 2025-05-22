package com.lootopiaApi.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "maps")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String skin;
    private String zone;
    private Integer scaleMin;
    private Integer scaleMax;

    @ManyToOne
    @JoinColumn(name = "hunt_id")
    @JsonBackReference
    private Hunt hunt;
}