package com.project.bakerhughesapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data // To String
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLogInDTO {

    @JsonProperty("phone-number")
    @NotBlank(message = "Phone Number cannot be empty")
    private String phoneNumber;


    @NotBlank(message = "Password cannot be empty")
    private String password;
}
