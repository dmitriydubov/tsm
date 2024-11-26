package com.example.tms.security.service.impl;

import com.example.tms.error.EmptyFieldException;
import com.example.tms.error.UserAlreadyExistException;
import com.example.tms.security.dto.SigninRequest;
import com.example.tms.security.dto.SignupRequest;
import com.example.tms.security.model.User;
import com.example.tms.security.repository.UserRepository;
import com.example.tms.security.service.RequestFormValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestFormValidationServiceImpl implements RequestFormValidationService {

    private final UserRepository userRepository;

    @Override
    public void validateEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) throw new UserAlreadyExistException("User already exist");
    }

    @Override
    public void validateEmptyFormFields(SignupRequest signupRequest) {
        if (signupRequest.email() == null || signupRequest.email().isEmpty() ||
                signupRequest.username() == null || signupRequest.username().isEmpty() ||
                signupRequest.password() == null || signupRequest.password().isEmpty() ||
                signupRequest.roles() == null || signupRequest.roles().isEmpty()
        ) {
            throw new EmptyFieldException("All fields are required");
        }
    }

    @Override
    public void validateEmptyFormFields(SigninRequest signinRequest) {
        if (signinRequest.email() == null || signinRequest.email().isEmpty() ||
                signinRequest.password() == null || signinRequest.password().isEmpty()
        ) {
            throw new EmptyFieldException("All fields are required");
        }
    }
}
