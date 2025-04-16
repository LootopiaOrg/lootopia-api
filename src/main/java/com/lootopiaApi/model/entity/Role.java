package com.lootopiaApi.model.entity;

import com.lootopiaApi.model.ERole;
import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private ERole role;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
