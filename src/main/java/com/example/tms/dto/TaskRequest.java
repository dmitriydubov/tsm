package com.example.tms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Сущность задачи")
public record TaskRequest(

        @Pattern(regexp = "\\d", message = "id задачи должен состоять из целого числа")
        @Max(Long.MAX_VALUE)
        @Schema(description = "Уникальный идентификатор пользователя", example = "1")
        String taskId,

        @NotBlank(message = "Поле 'Заголовок' не должно быть пустым")
        @Schema(description = "Заголовок задачи", example = "Сделать задачу Х")
        String title,

        @NotBlank(message = "Поле 'Описание задачи' не должно быть пустым")
        @Schema(description = "Описание задачи", example = "Описание задачи Х")
        String description,

        @Pattern(regexp = "(done)|(in progress)|(pending)", message = "В поле 'статус задачи' могут быть указаны только три значения - done, in progress, pending")
        @Schema(description = "Статус задачи", example = "done", defaultValue = "pending")
        String status,

        @NotBlank(message = "Поле 'Приоритет задачи' не должно быть пустым")
        @Pattern(regexp = "(high)|(medium)|(low)", message = "В поле 'приоритет задачи' могут быть указаны только три значения - high, medium, low")
        @Schema(description = "Приоритет задачи", example = "high", defaultValue = "low")
        String priority,

        @Schema(description = "Комментарий", example = "Комментарий к задаче Х")
        String comment,

        @NotBlank(message = "Поле 'Исполнитель задачи' не должно быть пустым")
        @Email(message = "Введите E-mail исполнителя")
        @Schema(description = "E-mail исполнителя", example = "user1@email.com")
        String assignee
) {
}
