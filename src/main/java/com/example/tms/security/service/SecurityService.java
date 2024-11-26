package com.example.tms.security.service;

import com.example.tms.security.dto.*;

public interface SecurityService {
    LoginResponse authenticateUser(SigninRequest signinRequest);
    RegisterResponse register(SignupRequest signupRequest);
    RefreshTokenResponse refreshToken(RequestTokenRefresh request);
    void logout();
}
