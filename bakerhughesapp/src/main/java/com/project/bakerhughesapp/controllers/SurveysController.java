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

    // Insert List Of Surveys
    @CrossOrigin(origins = "*")
    @PostMapping("/list")
    public ResponseEntity<ResponseObject> createListSurveys(
                                            @RequestBody List<SurveyDTO> surveys,
                                            BindingResult result) {
        // Check error messages
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Failed", "Unable to create survey list", errorMessages));
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("OK", "Create survey list successfully!", surveyService.createSurveyList(surveys)));
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

    // Update Sliding Data to existing survey
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

    // Find closest, so sanh list of depth with
    private float findClosest(List<Float> floatList, float a) {
        for (float d : floatList) {
            if (d > a)
                return d;
        }
        return 0;
    }

    // Calculator to update surveys
    public List<Survey> calculateSurvey(List<Survey> surveys) {
        // get tool from database, if null set all properties = 0
        Tool tool = toolRepository.findById(1).orElseGet(() -> new Tool(1L, 0f, 0f));

        if (surveys.isEmpty())
            return surveys;

        for (Survey survey : surveys) {
            Survey item = surveyService.getSurveyById(survey.getId());

            // Bit depth (sensor depth = tool.getSensorOffset)
            survey.setBitDepth(survey.getSurveyDepth() + tool.getSensorOffset());
            if (item != null) {
                item.setBitDepth(survey.getBitDepth());
                surveyRepository.save(item);
            }

            // DP depth
            if (survey.getId() == 1)
                survey.setDpLength((float)0);
            else {
                Survey prevSlide = surveyRepository.findById(survey.getId() - 1).orElse(null);
                if (prevSlide != null) {
                    float result = survey.getBitDepth() - prevSlide.getBitDepth();

                    survey.setDpLength((float) (Math.floor(result * 100)) / 100);
                    if (item != null) {
                        item.setDpLength(survey.getDpLength());
                        surveyRepository.save(item);
                    }
                }
            }

            // Total Slide distance
            survey.setTotalSlid((float) (Math.floor((survey.getEd() - survey.getSt()) * 100)) / 100);
            if (item != null) {
                item.setTotalSlid(survey.getTotalSlid());
                surveyRepository.save(item);
            }

            // SlideSeen || SlideIUnseen
            List<Float> surveyList = surveyRepository.findAll().stream()
                    .map(Survey::getSurveyDepth).toList();

            float closest = findClosest(surveyList, survey.getSt());

            if (closest == 0) {
                survey.setSlidSeen((float) 0);
                survey.setSlidUnseen((float) 0);
            } else {
                if (survey.getEd() < closest) {
                    survey.setSlidSeen(survey.getTotalSlid());
                    survey.setSlidUnseen((float) 0);
                } else {
                    survey.setSlidSeen((float) Math.round((closest - survey.getSt()) * 100) / 100);
                    survey.setSlidUnseen((float) Math.round((survey.getTotalSlid() - survey.getSlidSeen()) * 100) / 100);
                }
            }
            if (survey.getEd() == 0) {
                survey.setSlidSeen((float) 0);
                survey.setSlidUnseen((float) 0);
            }
            if (item != null) {
                item.setSlidSeen(survey.getSlidSeen());
                item.setSlidUnseen(survey.getSlidUnseen());
                surveyRepository.save(item);
            }

            // Meter seen
            if (survey.getId() == 3) {
                Survey prev2Survey = surveyRepository.findById(survey.getId() - 2).orElse(null);
                if (prev2Survey != null) {
                    survey.setTotalSeen(prev2Survey.getSlidSeen());
                    if (item != null) {
                        item.setTotalSeen(survey.getTotalSeen());
                        surveyRepository.save(item);
                    }
                }
            }
            if (survey.getId() > 3) {
                Survey prev3Slide = surveyRepository.findById(survey.getId() - 3).orElse(null);
                Survey prev2Slide = surveyRepository.findById(survey.getId() - 2).orElse(null);
                if (prev3Slide != null && prev2Slide != null) {
                    survey.setTotalSeen((float) (Math.floor((prev3Slide.getSlidUnseen() + prev2Slide.getSlidSeen()) * 100)) / 100);
                    if (item != null) {
                        item.setTotalSeen(survey.getTotalSeen());
                        surveyRepository.save(item);
                    }
                }
            }

            // Burm and Bur30m
            if (survey.getId() > 1) {
                // khi chua nhin thay meter seen -> lay gia tri default Motor Output
                if (survey.getTotalSeen() == 0) {
                    // get tool.defaultBur from database
                    survey.setBur30m(tool.getDefaultBUR());
                    survey.setBurm((float) (Math.floor(survey.getBur30m() * 100 / 30)) / 100);
                } else {
                    // nhin thay meter seen -> bur = Delta Inc  / meter seen
                    float incDiff =
                            Objects.requireNonNull(surveyRepository.findById(survey.getId()).orElse(null)).getInc()
                            - Objects.requireNonNull(surveyRepository.findById(survey.getId() - 1).orElse(null)).getInc();

                    float roundUpIncDiff = Math.round(incDiff * 100f) / 100f;
                    float totalSeen = Objects.requireNonNull(surveyRepository.findById(survey.getId()).orElse(null)).getTotalSeen();


                    survey.setBurm((float) Math.round((roundUpIncDiff / totalSeen)*100f) / 100f);
                    survey.setBur30m((float) (Math.round((survey.getBurm() * 30) * 100f)) / 100f);
                }
                if (item != null) {
                    item.setBurm(survey.getBurm());
                    item.setBur30m(survey.getBur30m());
                    surveyRepository.save(item);
                }
            }

            // Inc at bit
            if (survey.getId() == 2 || survey.getId() == 3) {
                if (survey.getTotalSeen() == 0) {
                    if (survey.getIncBit() == 0)
                        survey.setIncBit((float)
                                (Math.round(
                                        (Objects.requireNonNull(surveyRepository.findById(survey.getId() - 1).orElse(null)).getTotalSlid()
                                        * survey.getBurm() + survey.getInc()) * 100f)) / 100f);
                } else {
                    if (survey.getIncBit() == 0) {
                        survey.setIncBit((float)
                                (Math.round(
                                        ((Objects.requireNonNull(surveyRepository.findById(survey.getId() - 2).orElse(null)).getSlidUnseen() +
                                                Objects.requireNonNull(surveyRepository.findById(survey.getId() - 1).orElse(null)).getTotalSlid())
                                        * survey.getBurm() + survey.getInc()) * 100f)) / 100f);
                    }
                }
                if (item != null) {
                    item.setIncBit(survey.getIncBit());
                    surveyRepository.save(item);
                }
            }

            // khi co 2 gia tri BUR lien tiep thi lay gia tri trung binh theo @ meter seen cua 2 gia tri do
            if (survey.getId() > 3) {
                Optional<Survey> prevslide1 = surveyRepository.findById(survey.getId() - 1);
                assert prevslide1.orElse(null) != null;

                float prev1Percent = Math.round(prevslide1.orElse(null).getTotalSeen() / (prevslide1.orElse(null).getTotalSeen() + survey.getTotalSeen()) * 10f) /10f;
                float surveyPercent = 1 - prev1Percent;
                // calculate average BUR base on total seen of each survey
                float averageBur = prev1Percent * prevslide1.orElse(null).getBurm() + surveyPercent * survey.getBurm();

                if (survey.getIncBit() == 0)
                    survey.setIncBit((float) (Math.round(
                            ((Objects.requireNonNull(surveyRepository.findById(survey.getId() - 1).orElse(null)).getTotalSlid()
                                    + Objects.requireNonNull(surveyRepository.findById(survey.getId() - 2).orElse(null)).getSlidUnseen())
                                    * averageBur + survey.getInc()) * 100f)) / 100f);
                if (item != null) {
                    item.setIncBit(survey.getIncBit());
                    surveyRepository.save(item);
                }
            }
        }
        return surveys;
    }

    // Calculator Surveys
    @CrossOrigin(origins = "*")
    @GetMapping("/calculator-surveys") //http://localhost:8088/api/v1/surveys/calculator-surveys
    public ResponseEntity<ResponseObject> getAllCalculatorSurveys() {
        List<Survey> surveys = surveyService.getAllSurveys();

         surveys = calculateSurvey(surveys);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Show all calculated surveys successfully !", surveys)
        );
    }

}
