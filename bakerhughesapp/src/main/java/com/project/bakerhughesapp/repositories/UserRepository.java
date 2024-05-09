package com.project.bakerhughesapp.repositories;

import com.project.bakerhughesapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // check user da ton tai hay chua
    boolean existsByPhoneNumber(String phoneNumber);

    // Find User by Phone Number
    Optional<User> findByPhoneNumber(String phoneNumber);
}
