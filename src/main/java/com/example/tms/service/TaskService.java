package com.example.tms.service;

import com.example.tms.dto.TaskRemoveRequest;
import com.example.tms.dto.TaskRequest;
import com.example.tms.dto.TaskResponse;
import com.example.tms.dto.ViewTaskResponse;
import com.example.tms.model.Task;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;

public interface TaskService {

    TaskResponse createTask(@Valid TaskRequest taskRequest);

    TaskResponse removeTask(@Valid TaskRemoveRequest taskRemoveRequest);

    TaskResponse updateTask(@Valid TaskRequest taskRequest);

    ViewTaskResponse getTask(
            @Valid @NotEmpty @Pattern(regexp = "\\d+",  message = "id задачи должен состоять из целого числа")
            String taskId
    );

    Page<ViewTaskResponse> getAssigneeTask(String userId, String page, String size);
}
