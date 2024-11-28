package com.example.tms.service;

import com.example.tms.dto.TaskRemoveRequest;
import com.example.tms.dto.TaskRequest;
import com.example.tms.dto.TaskResponse;
import jakarta.validation.Valid;

public interface TaskService {

    TaskResponse createTask(@Valid TaskRequest taskRequest);

    TaskResponse removeTask(@Valid TaskRemoveRequest taskRemoveRequest);

    TaskResponse updateTask(@Valid TaskRequest taskRequest);
}
