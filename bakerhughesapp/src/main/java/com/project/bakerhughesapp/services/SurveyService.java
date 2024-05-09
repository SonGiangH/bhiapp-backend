package com.project.bakerhughesapp.services;

import com.project.bakerhughesapp.dtos.SurveyDTO;
import com.project.bakerhughesapp.models.Sliding;
import com.project.bakerhughesapp.models.Survey;
import com.project.bakerhughesapp.repositories.SurveyRepository;
import com.project.bakerhughesapp.repositories.ToolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //khong can tao constructor trong moi method nua
public class SurveyService implements ISurveyService {

    private final SurveyRepository surveyRepository;
    private final ToolRepository toolRepository;

    // create new survey
    @Override
    public Survey createSurvey(SurveyDTO surveyDTO) {
        // copy thuoc tinh tu DTO -> Survey
        Survey newSurvey = Survey.builder()
                .dpLength(surveyDTO.getDpLength())
                .surveyDepth(surveyDTO.getSurveyDepth())
                .inc(surveyDTO.getInc())
                .azi(surveyDTO.getAzi())
                .bitDepth(surveyDTO.getBitDepth() + toolRepository.findById(1).orElseThrow().getSensorOffset())
                .totalSeen(surveyDTO.getTotalseen())
                .dlsWa(surveyDTO.getDlsWa())
                .dls30m(surveyDTO.getDls30m())
                .motorYield(surveyDTO.getMotorYield())
                .toolFace(surveyDTO.getToolFace())
                .ed(surveyDTO.getEd())
                .st(surveyDTO.getSt())
                .totalSlid(surveyDTO.getTotalSlid())
                .slidSeen(surveyDTO.getSlidSeen())
                .slidUnseen(surveyDTO.getSlidUnseen())
                .build();

        return surveyRepository.save(newSurvey);
    }

    // get survey by ID
    @Override
    public Survey getSurveyById(long surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found"));
    }

    // get all surveys
    @Override
    public List<Survey> getAllSurveys() {
        return surveyRepository.findAll();
    }

    // update survey by ID (update survey depth, Inc, Azi, Total Seen, Bur, Bur30m, Inc at Bit, Slide see-unseen)
    @Override
    public Survey updateSurvey(long surveyId, SurveyDTO surveyDTO) {
        // get survey by ID
        Survey existingSurvey = getSurveyById(surveyId);

        // update with new survey needed information (Survey Depth - Inclination - Azimuth - DLS WA)
        existingSurvey.setBitDepth(surveyDTO.getSurveyDepth() + toolRepository.findById(1).orElseThrow().getSensorOffset());
        existingSurvey.setSurveyDepth(surveyDTO.getSurveyDepth());
        existingSurvey.setInc(surveyDTO.getInc());
        existingSurvey.setAzi(surveyDTO.getAzi());
        existingSurvey.setDlsWa(surveyDTO.getDlsWa());
        // update sliding data
        existingSurvey.setToolFace(surveyDTO.getToolFace());
        existingSurvey.setSt(surveyDTO.getSt());
        existingSurvey.setEd(surveyDTO.getEd());
        existingSurvey.setTotalSlid(surveyDTO.getEd()-surveyDTO.getSt());

        return surveyRepository.save(existingSurvey);
    }

    // delete survey by Id
    @Override
    public void deleteSurveyById(long surveyId) {
        // get existing survey by Id
        Optional<Survey> optionalSurvey = surveyRepository.findById(surveyId);
        // hard delete
        optionalSurvey.ifPresent(surveyRepository::delete);
    }

    // clear all survey
    @Override
    public void deleteAllSurveys() {
        // hard delete
        surveyRepository.deleteAll();
    }

    // truncate table to reset ID to 0 whenever clear all data
    @Override
    @Transactional
    public void truncateMyTable() {
        surveyRepository.truncateMyTable();
    }

    // Update sliding data for existing survey
    @Override
    public Sliding updateSliding(Sliding sliding) {

        // get survey by ID
        Survey existingSurvey = getSurveyById(sliding.getSurveyID());

        // update with Sliding information
        existingSurvey.setToolFace(sliding.getToolFace());
        existingSurvey.setSt(sliding.getSt());
        existingSurvey.setEd(sliding.getEd());

        surveyRepository.save(existingSurvey);
        return sliding;
    }
}
