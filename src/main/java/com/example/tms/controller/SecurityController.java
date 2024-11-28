package com.example.tms.controller;

import com.example.tms.dto.*;
import com.example.tms.service.impl.SecurityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityServiceImpl securityService;

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> signIn(@RequestBody SigninRequest signinRequest) {
        return ResponseEntity.ok(securityService.authenticateUser(signinRequest));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<RegisterResponse> signUp(@RequestBody SignupRequest signupRequest) {
        RegisterResponse response = securityService.register(signupRequest);
        return ResponseEntity.created(URI.create("/users/" + response.userId())).body(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RequestTokenRefresh tokenRefresh) {
        return ResponseEntity.ok(securityService.refreshToken(tokenRefresh));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        securityService.logout();
        return ResponseEntity.ok("Вы вышли из аккаунта");
    }
}
