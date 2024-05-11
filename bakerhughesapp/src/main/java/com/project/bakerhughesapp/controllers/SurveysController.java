package com.project.bakerhughesapp.controllers;

import com.project.bakerhughesapp.dtos.SurveyDTO;
import com.project.bakerhughesapp.models.ResponseObject;
import com.project.bakerhughesapp.models.Sliding;
import com.project.bakerhughesapp.models.Survey;
import com.project.bakerhughesapp.models.Tool;
import com.project.bakerhughesapp.repositories.SurveyRepository;
import com.project.bakerhughesapp.repositories.ToolRepository;
import com.project.bakerhughesapp.services.ISurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("api/v1/surveys")
// dependencies injection
@RequiredArgsConstructor
public class SurveysController {
    // Survey Service and Repository
    private final ISurveyService surveyService;
    private final SurveyRepository surveyRepository;
    private final ToolRepository toolRepository;

    // Insert New Survey
    @CrossOrigin(origins = "*")
    @PostMapping("")
    // Nếu tham số truyền vào là 1 đối tượng => DATA TRANSFER OBJECT = Request Object
    public ResponseEntity<ResponseObject> createSurvey(
                                            @Valid @RequestBody SurveyDTO surveyDTO,
                                            BindingResult result) {
        // Check error messages
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Failed", "Unable to create new survey", errorMessages)
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Create new survey successfully!", surveyService.createSurvey(surveyDTO))
        );
    }

    //Show all surveys
    @CrossOrigin(origins = "*")
    @GetMapping("") //http://localhost:8088/api/v1/surveys
    public ResponseEntity<ResponseObject> getAllSurveys() {

        List<Survey> allSurveys = surveyService.getAllSurveys();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Show all surveys successfully !", allSurveys)
        );
    }


    // Get survey by ID
    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getSurveyById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query survey successfully",
        surveyService.getSurveyById(id))
        );
    }

    // Edit Survey By ID
    @CrossOrigin(origins = "*")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateSurvey(@RequestBody SurveyDTO surveyDTO,
                                                       @PathVariable Long id) {

        Survey updatedSurvey = surveyService.updateSurvey(id, surveyDTO);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update survey successfully !",updatedSurvey)
        );
    }

    // Delete Survey by Id
    @CrossOrigin(origins = "*")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurveyById(id);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete survey successfully !",id)
        );
    }

    // Delete All Survey (clear data)
    @CrossOrigin(origins = "*")
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> clearData() {
        surveyService.deleteAllSurveys();
        // reset database start ID from 1 by truncate table from survey service
        surveyService.truncateMyTable();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Clear all surveys successfully !","")
        );
    }

    // Input Sliding data with given Survey ID
    @CrossOrigin(origins = "*")
    @PostMapping("/sliding")
    public ResponseEntity<ResponseObject> updateSliding(@RequestBody Sliding sliding,
                                                        BindingResult result) {
        // Check error messages
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Failed", "Unable to update sliding", errorMessages));
        }
        // call to service to save list
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Update survey Sliding successfully!", surveyService.updateSliding(sliding)));
    }

    // check seen - unseen and Total meter seen
    public List<Survey> checkSeenUnseen(List<Survey> surveys) {
        float meterSeen = 0;
        float meterAhead;
        // get tool from database, if null set all properties = 0
        Tool tool = toolRepository.findById(1).orElseGet(() -> new Tool(1L, 0f, 0f));

        // loop through all survey inside given survey list
        for (int i = 0; i < surveys.size(); i++) {
            // get gia tri survey hien tai
            Survey item = surveys.get(i);

            // Bit depth (sensor depth = tool.getSensorOffset)
            item.setBitDepth(item.getSurveyDepth() + tool.getSensorOffset());

            item.setBitDepth(item.getBitDepth());
            surveyRepository.save(item);


            // DP depth
            if (i == 0)
                item.setDpLength((float)0);
            else {
                Survey prevSlide = surveys.get(i - 1);
                if (prevSlide != null) {
                    float result = item.getBitDepth() - prevSlide.getBitDepth();

                    item.setDpLength((float) (Math.floor(result * 100)) / 100);
                    item.setDpLength(item.getDpLength());
                    surveyRepository.save(item);
                }
            }

            // Total Slide distance
            item.setTotalSlid((float) (Math.floor((item.getEd() - item.getSt()) * 100)) / 100);
                item.setTotalSlid(item.getTotalSlid());
                surveyRepository.save(item);

            // Seen - Unseen (loop through all survey to check)
            for (int j = 0; j < i ; j++) {
                Survey survey1 = surveys.get(j);

                if (survey1.getSlidSeen() != 0 && survey1.getSlidUnseen() != 0) {
                    continue;
                }

                // item fall into between survey1 (St;End)
                if ( item.getSurveyDepth() > survey1.getSt() &&
                        item.getSurveyDepth() < survey1.getEd()) {

                    survey1.setSlidSeen(item.getSurveyDepth()-survey1.getSt());
                    survey1.setSlidUnseen(survey1.getTotalSlid() - survey1.getSlidSeen());
                    surveyRepository.save(survey1);
                } else if (item.getSurveyDepth() > survey1.getEd() && survey1.getEd() != 0) {
                    survey1.setSlidSeen(survey1.getTotalSlid());
                    survey1.setSlidUnseen((float) 0);
                    surveyRepository.save(survey1);
                } else {
                    survey1.setSlidSeen((float) 0);
                    survey1.setSlidUnseen((float) 0);
                    surveyRepository.save(survey1);
                }
            }

            /* Meter seen */
            // Previous survey of item
            for (int j = 0; j <=i; j++) {

                // Survey dau tien trong lis total Seen = 0
                if (i-1 <0) {
                    item.setTotalSeen((float) 0);
                    break;
                }

                Survey previousSurvey = surveys.get(i - 1);

                // Prev survey fall inside survey(j) (St -End)
                if (surveys.get(j).getSt() < previousSurvey.getSurveyDepth() &&
                        surveys.get(j).getEd() > previousSurvey.getSurveyDepth()) {
                        meterSeen = surveys.get(j).getSlidUnseen();

                        // check item - current survey
                        if (surveys.get(j+1).getSt() < item.getSurveyDepth() &&
                        surveys.get(j+1).getEd() > item.getSurveyDepth()) {
                            meterSeen += surveys.get(j+1).getSlidSeen();
                        }
                        if ( surveys.get(j+1).getEd() < item.getSurveyDepth()) {
                            meterSeen += surveys.get(j+1).getTotalSlid();
                        }
                        item.setTotalSeen(meterSeen);
                        surveyRepository.save(item);
                        break;
                }

                // item fall inside survey(j) St-End
                if (surveys.get(j).getSt() < item.getSurveyDepth() &&
                        surveys.get(j).getEd() > item.getSurveyDepth()) {
                        meterSeen = surveys.get(j).getSlidSeen();

                        for (int m =0; m <= i; m++) {
                            if (previousSurvey.getSurveyDepth() > surveys.get(m).getSt() &&
                                previousSurvey.getSurveyDepth() < surveys.get(m).getEd()) {
                                meterSeen += surveys.get(m).getSlidUnseen();
                            }
                        }
                        item.setTotalSeen(meterSeen);
                        break;
                }
                else if (item.getSurveyDepth() > surveys.get(j).getEd() &&
                        surveys.get(j).getEd() != 0 &&
                        previousSurvey.getSurveyDepth() < surveys.get(j).getSt() &&
                        surveys.get(j).getSt() !=0) {
                            meterSeen = surveys.get(j).getTotalSlid();
                            item.setTotalSeen(meterSeen);
                            surveyRepository.save(item);
                            break;
                }

            }

            /*Calculate the DLS /30m */
            if (i == 0) {
                item.setDls30m((float) 0);
            }
            else {
                Survey prevSurvey = surveys.get(i - 1);
                if (prevSurvey != null) {
                    float dls30m = calculateDLS(prevSurvey, item);
                    item.setDls30m(dls30m);
                    surveyRepository.save(item);
                }
            }

            /* Calculate Motor Yield*/
            if (item.getDls30m() == 0 || item.getTotalSeen() == 0 || i==0) {
                item.setMotorYield((float) 0);
            } else {
                Survey prevSurvey = surveys.get(i - 1);
                float percentSlide = item.getTotalSeen() / (item.getSurveyDepth() - prevSurvey.getSurveyDepth());
                float motorYield = item.getDls30m() / percentSlide;
                item.setMotorYield(motorYield);
                surveyRepository.save(item);
            }

            /* Calculate Meter Ahead */
            // check between : item.surveyDepth -- item.bitDepth, check item.surveyDepth first
            for (int j = 0; j <= i; j++) {

                meterAhead = 0;
                // Survey Depth fall inside sliding
                if (item.getSurveyDepth() > surveys.get(j).getSt() &&
                        item.getSurveyDepth()<surveys.get(j).getEd()) {
                    meterAhead = surveys.get(j).getEd() - item.getSurveyDepth();

                    // check Bit Depth - loop through surveys from j - i
                    for (int m= j+1; m <= i; m++) {
                        if (item.getBitDepth() > surveys.get(m).getEd()) {
                            meterAhead += surveys.get(m).getTotalSlid();
                        } else if (item.getBitDepth() > surveys.get(m).getSt() &&
                                item.getBitDepth() < surveys.get(m).getEd()) {
                            meterAhead += item.getBitDepth() - surveys.get(m).getSt();
                        }
                    }
                    item.setMeterAhead(meterAhead);
                    surveyRepository.save(item);
                    break; // Exit the loop since we found the relevant survey
                }


                // Survey Depth fall outside sliding - cover sliding (fall above)
                if (item.getSurveyDepth() < surveys.get(j).getSt()) {

                    // check Bit Depth - loop through surveys from j-i
                    for (int m=j; m <= i; m++) {
                        // Bit Depth falling inside surveys m
                        if (item.getBitDepth() > surveys.get(m).getSt() &&
                                item.getBitDepth() < surveys.get(m).getEd()) {
                            meterAhead = item.getBitDepth() - surveys.get(m).getSt();
                        }
                        // Bit depth exceed the sliding
                        else if (item.getBitDepth() > surveys.get(m).getEd() &&
                                surveys.get(m).getEd() != 0 ) {
                            meterAhead = surveys.get(m).getTotalSlid();
                        }
                    }
                    item.setMeterAhead(meterAhead);
                    surveyRepository.save(item);
                    break; // Exit the loop since we found the relevant survey
                }
            }
        }
        return  surveys;
    }

    // calculate DLS of between 2 surveys
    public Float calculateDLS(Survey surveyStart, Survey surveyEnd) {
        float dls30m = 0;
        double a = Math.sin(Math.toRadians(Math.abs(surveyEnd.getAzi() - surveyStart.getAzi()))) * surveyEnd.getInc();
        double b = Math.sqrt(Math.pow(surveyEnd.getInc(), 2) - Math.pow(a, 2));
        double c = b - surveyStart.getInc();

        double dlsPerLength = Math.sqrt(Math.pow(a, 2) + Math.pow(c, 2));

        dls30m = (float) (dlsPerLength * 30 / (surveyEnd.getSurveyDepth() - surveyStart.getSurveyDepth()));

        return dls30m;
    }

    // Calculator Surveys
    @CrossOrigin(origins = "*")
    @GetMapping("/calculator-surveys") //http://localhost:8088/api/v1/surveys/calculator-surveys
    public ResponseEntity<ResponseObject> getAllCalculatorSurveys() {
        List<Survey> surveys = surveyService.getAllSurveys();

         surveys = checkSeenUnseen(surveys);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Show all calculated surveys successfully !", surveys)
        );
    }

}
