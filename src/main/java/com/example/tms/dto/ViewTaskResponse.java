package com.example.tms.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Сущность задачи в ответе")
public record ViewTaskResponse(
        @Schema(description = "id задачи")
        String taskId,

        @Schema(description = "Заголовок задачи")
        String title,

        @Schema(description = "Описание задачи")
        String description,

        @Schema(description = "Статус выполнения")
        String status,

        @Schema(description = "Приоритет задачи")
        String priority,

        @Schema(description = "Список комментариев")
        List<CommentDTO> comments,

        @Schema(description = "Автор задачи")
        String author,

        @Schema(description = "Исполнитель задачи")
        String assignee,

        @Schema(description = "Дата создания задачи")
        String creationDate
) {
}
