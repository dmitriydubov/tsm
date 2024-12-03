package com.example.tms.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сущность запроса на обновление токена jwt")
public record RequestTokenRefresh(

        @Schema(description = "Refresh token")
        String tokenRefresh
) {
}
