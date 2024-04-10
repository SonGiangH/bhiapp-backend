package com.project.bakerhughesapp.services;

import com.project.bakerhughesapp.dtos.SurveyDTO;
import com.project.bakerhughesapp.models.Sliding;
import com.project.bakerhughesapp.models.Survey;
import com.project.bakerhughesapp.repositories.SurveyRepository;
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

    // create new survey
    @Override
    public Survey createSurvey(SurveyDTO surveyDTO) {
        // copy thuoc tinh tu DTO -> Survey
        Survey newSurvey = Survey.builder()
                .dpLength(surveyDTO.getDpLength())
                .surveyDepth(surveyDTO.getSurveyDepth())
                .inc(surveyDTO.getInc())
                .azi(surveyDTO.getAzi())
                .bitDepth(surveyDTO.getBitDepth())
                .totalSeen(surveyDTO.getTotalseen())
                .burm(surveyDTO.getBurm())
                .bur30m(surveyDTO.getBur30m())
                .incBit(surveyDTO.getIncBit())
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

        // update with new survey needed information
        existingSurvey.setBitDepth(surveyDTO.getBitDepth());
        existingSurvey.setSurveyDepth(surveyDTO.getSurveyDepth());
        existingSurvey.setInc(surveyDTO.getInc());
        existingSurvey.setAzi(surveyDTO.getAzi());
        existingSurvey.setTotalSeen(surveyDTO.getTotalseen());
        existingSurvey.setBurm(surveyDTO.getBurm());
        existingSurvey.setBur30m(surveyDTO.getBur30m());
        existingSurvey.setIncBit(surveyDTO.getIncBit());
        existingSurvey.setSlidSeen(surveyDTO.getSlidSeen());
        existingSurvey.setSlidUnseen(surveyDTO.getSlidUnseen());

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

    // Create survey List to database
    @Override
    public List<Survey> createSurveyList(List<SurveyDTO> surveyDTOs) {
        List<Survey> surveys = new ArrayList<>();

        for (SurveyDTO surveyDTO: surveyDTOs) {
            surveys.add(createSurvey(surveyDTO));
        }
        return surveys;
    }

    // Xu ly update khi input sliding data
    @Override
    public Sliding updateSliding(Sliding sliding) {

        // get all survey from db
        List<Survey> surveyList = surveyRepository.findAll();

        List<Sliding> slidingList = new ArrayList<>();

        if (!surveyList.isEmpty()) {
            for (Survey survey : surveyList) {
                if (survey.getSt() == 0 || survey.getEd() == 0) {
                    continue;
                }

                Sliding tempSliding = new Sliding();
                tempSliding.setToolFace(survey.getToolFace());
                tempSliding.setSt(survey.getSt());
                tempSliding.setEd(survey.getEd());
                slidingList.add(tempSliding);
            }
        }

        Sliding newSlide = new Sliding();
        newSlide.setToolFace(sliding.getToolFace());
        newSlide.setSt(sliding.getSt());
        newSlide.setEd(sliding.getEd());
        slidingList.add(newSlide);

        slidingList.sort(Comparator.comparingDouble(Sliding::getSt));

        for (int i = 0; i < slidingList.size(); i++) {
            Survey survey = surveyList.get(i);
            Sliding slide = slidingList.get(i);
            survey.setToolFace(slide.getToolFace());
            survey.setSt(slide.getSt());
            survey.setEd(slide.getEd());
        }

        surveyRepository.saveAll(surveyList);

        return newSlide;
    }
}
