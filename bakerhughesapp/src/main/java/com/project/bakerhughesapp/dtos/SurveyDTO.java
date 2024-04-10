package com.project.bakerhughesapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data // To String
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SurveyDTO {

    @JsonProperty("dp_length")
    private Float dpLength;

    @JsonProperty("bit_depth")
    @NotNull(message = "Bit Depth cannot be empty")
    private Float bitDepth;

    @NotNull(message = "Survey Depth cannot be empty")
    @JsonProperty("survey_depth")
    private Float surveyDepth;

    @JsonProperty("inc")
    @NotNull(message = "Inclination cannot be empty")
    private Float inc;

    @JsonProperty("azi")
    @NotNull(message = "Azimuth cannot be empty")
    private Float azi;

    @JsonProperty("totalseen")
    private Float totalseen;

    @NotNull(message = "BUR/m cannot be empty")
    @JsonProperty("burm")
    private Float burm;

    @NotNull(message = "BUR/30m cannot be empty")
    @JsonProperty("bur30m")
    private Float bur30m;

    @JsonProperty("incbit")
    private Float incBit;

    @JsonProperty("toolface")
    private String toolFace;

    @NotNull(message = "Slide start cannot be empty")
    @JsonProperty("st")
    private Float st;

    @NotNull(message = "Slide End cannot be empty")
    @JsonProperty("ed")
    private Float ed;

    @NotNull(message = "totalSlidDist cannot be empty")
    @JsonProperty("totalslid")
    private Float totalSlid;

    @NotNull(message = "totalSlidSeen cannot be empty")
    @JsonProperty("slidseen")
    private Float slidSeen;

    @NotNull(message = "totalSlidUnseen cannot be empty")
    @JsonProperty("slidunseen")
    private Float slidUnseen;
}
