package com.lootopiaApi.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "email_confirmation_tokens")
public class EmailConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime timeStamp;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
