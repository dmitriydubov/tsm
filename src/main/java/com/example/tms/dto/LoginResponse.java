package com.example.tms.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Сущность ответа запроса на авторизацию/аутентификацию")
public record LoginResponse(

        @Schema(description = "Логин пользователя")
        Long id,

        @Schema(description = "токен jwt")
        String token,

        @Schema(description = "refresh token")
        String refreshToken,

        @Schema(description = "E-mail пользователя")
        String email,

        @Schema(description = "Список ролей пользователя")
        List<String> roles) {
}
