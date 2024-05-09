package com.project.bakerhughesapp.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "users")
@Data // To String
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // khi insert vao thi se autoincrement by 1
    private Long id;

    @Column(name = "fullname", length = 100)
    private String fullName;

    @Column(name="phone_number", length = 10, nullable = false)
    private String phoneNumber;

    @Column(name="address", length = 200)
    private String address;

    @Column(name="password", length = 200, nullable = false)
    private String password;

    @Column(name="created_at")
    private Date createdAt;

    @Column(name="updated_at")
    private Date updatedAt;

    @Column(name = "is_active")
    private boolean active;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "facebook_account_id")
    private Long faceBookId;

    @Column(name="google_account_id")
    private Long googleId;

    @ManyToOne
    @JoinColumn(name ="role_id")
    private Role role;
}
