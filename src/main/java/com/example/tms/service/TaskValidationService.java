package com.example.tms.service;

import com.example.tms.dto.TaskRequest;

public interface TaskValidationService {
    void validateTask(TaskRequest taskRequest);
}
