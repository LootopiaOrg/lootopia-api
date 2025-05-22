package com.lootopiaApi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lootopiaApi.model.annotations.ValidHuntDates;
import com.lootopiaApi.model.enums.HuntLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hunts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ValidHuntDates
public class Hunt extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private HuntLevel level;

    private String mode; // "réel" ou "cartographique"
    private String accessMode; // "public" ou "private"
    private Boolean chatEnabled;
    private Integer maxParticipants;
    private Integer participationFee;
    private Integer digDelaySeconds;

    @Column(columnDefinition = "TEXT")
    private String image;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    @JsonIgnore
    private User organizer;

    @OneToMany(mappedBy = "hunt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HuntStep> steps = new ArrayList<>();

    @OneToMany(mappedBy = "hunt", cascade = CascadeType.ALL)
    private List<Reward> rewards = new ArrayList<>();

    @OneToMany(mappedBy = "hunt", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MapConfig> maps = new ArrayList<>();
}