package com.example.tms.service.impl;

import com.example.tms.dto.TaskRequest;
import com.example.tms.dto.TaskResponse;
import com.example.tms.model.*;
import com.example.tms.repository.TaskRepository;
import com.example.tms.service.TaskService;
import com.example.tms.service.TaskValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskValidationService taskValidationService;

    @Override
    @Transactional
    public TaskResponse createTask(TaskRequest taskRequest) {
        // дописать валидацию задач
        taskValidationService.validateTask(taskRequest);

        Task task = Task.builder()
                .title(taskRequest.title())
                .description(taskRequest.description())
                .status(Status.STATUS_PENDING)
                .priority(getPriorityFromTaskRequest(taskRequest))
                .comments(new HashSet<>(taskRequest.comments().stream()
                        .map(comment -> Comment.builder().message(comment).date(new Date()).build())
                        .toList()))
                .author(Author.builder().name(taskRequest.author()).build())
                .contractor(Contractor.builder().name(taskRequest.contractor()).build())
                .date(new Date())
                .build();

        Task savedTask = taskRepository.saveAndFlush(task);

        return new TaskResponse(savedTask.getId(), savedTask.getTitle(), formatDate(savedTask.getDate()));
    }

    private Priority getPriorityFromTaskRequest(TaskRequest taskRequest) {
        return switch (taskRequest.priority().toUpperCase()) {
            case "MEDIUM" -> Priority.PRIORITY_MEDIUM;
            case "HIGH" -> Priority.PRIORITY_HIGH;
            default -> Priority.PRIORITY_LOW;
        };
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }
}
