package com.example.tms.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сущность ответа запроса на регистрацию")
public record RegisterResponse(

        @Schema(description = "Уникальный логин пользователя", example = "2")
        Long userId,

        @Schema(description = "E-mail пользователя")
        String email,

        @Schema(description = "Токен jwt")
        String token) {
}
