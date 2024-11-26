package com.example.tms.security.service;

import com.example.tms.security.dto.SigninRequest;
import com.example.tms.security.dto.SignupRequest;

public interface RequestFormValidationService {

    void validateEmail(String email);
    void validateEmptyFormFields(SignupRequest signupRequest);
    void validateEmptyFormFields(SigninRequest signinRequest);
}
