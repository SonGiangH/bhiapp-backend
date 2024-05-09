package com.project.bakerhughesapp.controllers;


import com.project.bakerhughesapp.dtos.UserDTO;
import com.project.bakerhughesapp.dtos.UserLogInDTO;
import com.project.bakerhughesapp.models.ResponseObject;
import com.project.bakerhughesapp.services.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    // register new user
    @PostMapping("/register")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
            ) {

        try {
            // Check error messages
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ResponseObject("Failed", "Unable to register user", errorMessages)
                );
            }
            // Check password and retype password must be matched
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body("Password does not match !");
            }

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("OK",
                            "Register User successfully!",
                            userService.registerUser(userDTO)));

        } catch (Exception e) {
                return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> logInUser(
            @Valid @RequestBody UserLogInDTO userLogInDTO,
            BindingResult result
    ) {
        // Kiem tra thong tin Log In va generate token
        // Return token trong response
        return ResponseEntity.ok("User log in successfully");
    }

}
