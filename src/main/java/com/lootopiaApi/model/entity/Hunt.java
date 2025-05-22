package com.lootopiaApi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lootopiaApi.model.annotations.ValidHuntDates;
import com.lootopiaApi.model.enums.HuntLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(columnDefinition = "TEXT")
    private String image;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    @JsonIgnore
    private User partnerId;

    @OneToMany(mappedBy = "hunt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HuntStep> steps = new ArrayList<>();
}
