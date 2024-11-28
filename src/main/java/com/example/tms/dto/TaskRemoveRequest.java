package com.example.tms.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;

public record TaskRemoveRequest(

        @Pattern(regexp = "\\d+", message = "id задачи должен состоять из целого числа")
        @Max(Long.MAX_VALUE)
        String taskId
) {
}
