package com.project.bakerhughesapp.repositories;
import com.project.bakerhughesapp.models.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey,Long> {

    boolean existsById(long surveyId);

    // find survey by Id
    Optional<Survey> findById(long surveyId);

    // get all surveys (SELECT * FROM surveys)
    List<Survey> findAll();

    // truncate the table
    @Modifying
    @Query(
            value = "truncate table surveys",
            nativeQuery = true
    )
    void truncateMyTable();
}
