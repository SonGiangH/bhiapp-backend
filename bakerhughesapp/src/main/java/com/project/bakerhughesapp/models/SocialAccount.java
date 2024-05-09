package com.project.bakerhughesapp.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "social_accounts")
@Data // To String
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // khi insert vao thi se autoincrement by 1
    private Long id;

    @Column(name = "provider", length = 20, nullable = false)
    private String provider;

    @Column(name = "provider_id", length = 50, nullable = false)
    private String providerId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;
}
