package com.example.tms.dto;

import jakarta.validation.constraints.*;

public record TaskRequest(

        @Pattern(regexp = "\\d", message = "id задачи должен состоять из целого числа")
        @Max(Long.MAX_VALUE)
        String taskId,

        @NotBlank(message = "Поле 'Заголовок' не должно быть пустым")
        String title,

        @NotBlank(message = "Поле 'Описание задачи' не должно быть пустым")
        String description,

        @Pattern(regexp = "(done)|(in progress)|(pending)", message = "В поле 'статус задачи' могут быть указаны только три значения - done, in progress, pending")
        String status,

        @NotBlank(message = "Поле 'Приоритет задачи' не должно быть пустым")
        @Pattern(regexp = "(high)|(medium)|(low)", message = "В поле 'приоритет задачи' могут быть указаны только три значения - high, medium, low")
        String priority,

        String comment,

        @NotBlank(message = "Поле 'Исполнитель задачи' не должно быть пустым")
        @Email(message = "Введите E-mail исполнителя")
        String assignee
) {
}
