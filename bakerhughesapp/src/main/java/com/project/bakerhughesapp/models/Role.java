package com.project.bakerhughesapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Data // To String
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // khi insert vao thi se autoincrement by 1
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
