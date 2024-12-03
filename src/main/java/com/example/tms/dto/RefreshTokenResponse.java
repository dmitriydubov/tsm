package com.example.tms.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сущность refresh token ответа")
public record RefreshTokenResponse(

        @Schema(description = "Токен jwt")
        String accessToken,

        @Schema(description = "refresh token")
        String refreshToken) {
}
