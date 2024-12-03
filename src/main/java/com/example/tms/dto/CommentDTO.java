package com.example.tms.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сущность комментария")
public record CommentDTO(

        @Schema(description = "id комментария", accessMode = Schema.AccessMode.READ_ONLY)
        String id,

        @Schema(description = "Сообщение")
        String message,

        @Schema(description = "Дата создания комментария")
        String creationDate) {
}
