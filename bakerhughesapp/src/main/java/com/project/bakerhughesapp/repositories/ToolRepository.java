package com.project.bakerhughesapp.repositories;

import com.project.bakerhughesapp.models.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    boolean existsById(long surveyId);

    // get tool by ID
    Optional<Tool> findById(long toolID);

    // truncate the table
    @Modifying
    @Query(
            value = "truncate table tool",
            nativeQuery = true
    )
    void truncateMyTable();
}
