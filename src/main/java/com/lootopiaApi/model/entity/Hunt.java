package com.lootopiaApi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Hunt extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private HuntLevel level;

    private String image;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    @JsonIgnore
    private User creator;
}
