package com.project.bakerhughesapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Data // To String
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {


    @JsonProperty("full-name")
    private String fullName;

    @JsonProperty("phone-number")
    @NotBlank(message = "Phone Number cannot be empty")
    private String phoneNumber;

    private String address;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @JsonProperty("retype-password")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private Date dob;

    @JsonProperty("facebook_account_id")
    private Long faceBookId;

    @JsonProperty("google_account_id")
    private Long googleId;

    @NotNull(message = "Role ID is required")
    @JsonProperty("role-id")
    private Long roleId;
}
