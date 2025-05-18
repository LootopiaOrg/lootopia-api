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

    @Lob
    @Column(columnDefinition = "TEXT")
    private String image;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    @JsonIgnore
    private User creator;
}
