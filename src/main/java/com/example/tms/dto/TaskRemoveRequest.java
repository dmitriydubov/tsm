package com.example.tms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Сущность запроса на удаление")
public record TaskRemoveRequest(

        @Pattern(regexp = "\\d+", message = "id задачи должен состоять из целого числа")
        @Max(Long.MAX_VALUE)
        @Schema(description = "Уникальный идентификатор задачи", example = "1")
        String taskId
) {
}
