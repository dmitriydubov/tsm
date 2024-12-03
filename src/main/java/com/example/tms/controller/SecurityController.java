package com.example.tms.controller;

import com.example.tms.dto.*;
import com.example.tms.service.impl.SecurityServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Пользователи", description = "Контроллер для регистрации/авторизации и аутентификации/обновления токенов jwt.")
public class SecurityController {

    private final SecurityServiceImpl securityService;

    @PostMapping("/sign-in")
    @Operation(summary = "Авторизация/аутентификация", description = "Позволяет пользователям авторизоваться")
    public ResponseEntity<LoginResponse> signIn(@RequestBody SigninRequest signinRequest) {
        return ResponseEntity.ok(securityService.authenticateUser(signinRequest));
    }

    @PostMapping("/sign-up")
    @Operation(summary = "Регистрация", description = "Позволяет пользователям регистрироваться")
    public ResponseEntity<RegisterResponse> signUp(@RequestBody SignupRequest signupRequest) {
        RegisterResponse response = securityService.register(signupRequest);
        return ResponseEntity.created(URI.create("/users/" + response.userId())).body(response);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Обновление токена jwt", description = "Позволяет обновлять просроченные токены jwt")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RequestTokenRefresh tokenRefresh) {
        return ResponseEntity.ok(securityService.refreshToken(tokenRefresh));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout пользователя", description = "Позволяет пользователям выйти из аккаунта")
    public ResponseEntity<?> logout() {
        securityService.logout();
        return ResponseEntity.ok("Вы вышли из аккаунта");
    }
}
