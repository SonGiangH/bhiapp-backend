package com.project.bakerhughesapp.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Data // To String
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // khi insert vao thi se autoincrement by 1
    private Long id;

    @Column(name = "token", nullable = false, unique = true)
    private  String token;

    @Column(name = "token_type", nullable = false, length = 50)
    private String tokenType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "revoked")
    private boolean revoked;

    @Column(name = "expired")
    private boolean expired;

    // Foreign keys with User ID, One User -> many token
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
