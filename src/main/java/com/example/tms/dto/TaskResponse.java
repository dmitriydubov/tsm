package com.example.tms.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сущность ответа сервера")
public record TaskResponse(

        @Schema(description = "Сообщение сервера")
        String Message,

        @Schema(description = "id задачи")
        Long id,

        @Schema(description = "Заголовок задачи")
        String title,

        @Schema(description = "Дата изменений")
        String creationTime) {
}
