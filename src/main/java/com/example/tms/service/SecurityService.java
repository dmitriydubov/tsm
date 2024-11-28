package com.example.tms.service;

import com.example.tms.dto.*;
import jakarta.validation.Valid;

public interface SecurityService {

    LoginResponse authenticateUser(@Valid SigninRequest signinRequest);

    RegisterResponse register(@Valid SignupRequest signupRequest);

    RefreshTokenResponse refreshToken(RequestTokenRefresh request);

    void logout();
}
