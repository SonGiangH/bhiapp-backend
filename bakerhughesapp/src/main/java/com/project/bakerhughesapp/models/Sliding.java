package com.project.bakerhughesapp.models;

import lombok.*;

@Data // To String
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sliding {
    private Long surveyID;
    private String toolFace;
    private Float st;
    private Float ed;

}
