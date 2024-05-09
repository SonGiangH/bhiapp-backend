package com.project.bakerhughesapp.services;

import com.project.bakerhughesapp.dtos.UserDTO;
import com.project.bakerhughesapp.dtos.UserLogInDTO;
import com.project.bakerhughesapp.models.User;

import java.util.List;

public interface IUserService {

    // register new User
    User registerUser(UserDTO userDTO);

    // log In User
    String logInUser(String phoneNumber, String password);

    // get all users
    List<User> getAllUsers();
}
