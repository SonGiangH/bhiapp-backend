package com.project.bakerhughesapp.services;

import com.project.bakerhughesapp.dtos.UserDTO;
import com.project.bakerhughesapp.dtos.UserLogInDTO;
import com.project.bakerhughesapp.models.User;
import com.project.bakerhughesapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements  IUserService{
    private final UserRepository userRepository;
    @Override
    public User registerUser(UserDTO userDTO) {
        // check existing user by phone number
        if (userRepository.existsByPhoneNumber(userDTO.getPhoneNumber())) {
         throw new DataIntegrityViolationException("Phone Number exist !");
        }
        // copy thuoc tinh tu DTO -> Model : UserDTO -> User Model
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDob())
                .faceBookId(userDTO.getFaceBookId())
                .googleId(userDTO.getGoogleId())
                .build();

        // check neu khong co tk google / facebook thi moi yeu cau password
        if (userDTO.getGoogleId() ==0 && userDTO.getFaceBookId() == 0) {
            String password = userDTO.getPassword();
        }

        return userRepository.save(newUser);
    }
    @Override
    public String logInUser(String phoneNumber, String password) {

        // log in thanh cong thi return ve token
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }
}
