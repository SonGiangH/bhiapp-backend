package com.project.bakerhughesapp.services;


import com.project.bakerhughesapp.dtos.SurveyDTO;
import com.project.bakerhughesapp.models.Sliding;
import com.project.bakerhughesapp.models.Survey;

import java.util.List;

public interface ISurveyService {
    // create new survey
    Survey createSurvey(SurveyDTO surveyDTO);

    // get all surveys
    List<Survey> getAllSurveys();

    // Update existing survey
    Survey updateSurvey(long surveyId, SurveyDTO surveyDTO);

    // delete survey by Id
    void deleteSurveyById(long surveyId);

    Survey getSurveyById(long id);

    void deleteAllSurveys();

    void truncateMyTable();

    Sliding updateSliding(Sliding sliding);
}
