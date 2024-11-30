package com.example.tms.dto;

import java.util.List;

public record ViewTaskResponse(
        String taskId,
        String title,
        String description,
        String status,
        String priority,
        List<CommentDTO> comments,
        String author,
        String assignee,
        String creationDate
) {
}
