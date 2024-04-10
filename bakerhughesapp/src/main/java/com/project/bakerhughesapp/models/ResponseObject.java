package com.project.bakerhughesapp.models;

import lombok.*;

@Data // To String
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ResponseObject {
    private String status;
    private String message;
    private Object data;

}
