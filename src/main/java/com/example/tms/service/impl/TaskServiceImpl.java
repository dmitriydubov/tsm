package com.example.tms.service.impl;

import com.example.tms.dto.TaskRemoveRequest;
import com.example.tms.dto.TaskRequest;
import com.example.tms.dto.TaskResponse;
import com.example.tms.error.NoSuchAssigneeException;
import com.example.tms.model.*;
import com.example.tms.repository.CommentsRepository;
import com.example.tms.repository.TaskRepository;
import com.example.tms.repository.UserRepository;
import com.example.tms.service.TaskService;
import com.example.tms.service.ValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentsRepository commentsRepository;
    private final ValidationService validationService;

    @Override
    @Transactional
    public TaskResponse createTask(@Valid TaskRequest taskRequest) {
        var currentAdmin = getCurrentUser();
        validationService.validateByAdminRole(currentAdmin);

        var taskAssignee = getCurrentAssignee(taskRequest);

        Task task = createNewTask(taskRequest, currentAdmin, taskAssignee);
        Task savedTask = taskRepository.saveAndFlush(task);

        return new TaskResponse(
                "Задача успешно добавлена",
                savedTask.getId(),
                savedTask.getTitle(),
                formatDate(savedTask.getDate())
        );
    }

    @Override
    @Transactional
    public TaskResponse removeTask(@Valid TaskRemoveRequest taskRemoveRequest) {
        var currentAdmin = getCurrentUser();
        validationService.validateByAdminRole(currentAdmin);

        var taskToRemove = validationService.validateByExistingTaskAndGet(Long.parseLong(taskRemoveRequest.taskId()));
        commentsRepository.deleteAllByTaskId(taskToRemove.getId());

        taskRepository.delete(taskToRemove);

        return new TaskResponse(
                "Задача успешно удалена",
                taskToRemove.getId(),
                taskToRemove.getTitle(),
                formatDate(new Date())
        );
    }

    @Override
    public TaskResponse updateTask(@Valid TaskRequest taskRequest) {
        var currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getRoles().contains(Role.ROLE_ADMIN);
        long taskId =Long.parseLong(taskRequest.taskId());

        var taskToUpdate = validationService.validateByExistingTaskAndGet(taskId);
        var taskAssignee = getCurrentAssignee(taskRequest);

        Task updatedTask;
        if (isAdmin) {
            updatedTask = update(taskRequest, taskToUpdate, currentUser, taskAssignee);
        } else {
            validationService.validateTaskByAssignee(currentUser, taskId);
            updatedTask = update(taskRequest, taskToUpdate);
        }

        return new TaskResponse(
                "Задача успешно изменена",
                updatedTask.getId(),
                updatedTask.getTitle(),
                formatDate(updatedTask.getDate())
        );
    }

    private Task update(TaskRequest taskRequest, Task taskToUpdate, User taskAuthor, User taskAssignee) {
        Task savedTask;
        taskToUpdate.setTitle(taskRequest.title());
        taskToUpdate.setDescription(taskRequest.description());
        if (taskRequest.status() != null && !taskRequest.status().isEmpty()) {
            taskToUpdate.setStatus(getStatusFromTaskRequest(taskRequest));
        }
        taskToUpdate.setPriority(getPriorityFromTaskRequest(taskRequest));
        taskToUpdate.getComments().add((Comment.builder().message(taskRequest.comment()).date(new Date()).build()));
        taskToUpdate.setAuthor(taskAuthor);
        taskToUpdate.setAssignee(taskAssignee);
        taskToUpdate.setDate(new Date());

        savedTask = taskRepository.save(taskToUpdate);
        return savedTask;
    }

    private Task update(TaskRequest taskRequest, Task taskToUpdate) {
        Task savedTask;
        if (taskRequest.status() != null && !taskRequest.status().isEmpty()) {
            taskToUpdate.setStatus(getStatusFromTaskRequest(taskRequest));
        }
        taskToUpdate.getComments().add(Comment.builder().message(taskRequest.comment()).date(new Date()).build());

        savedTask = taskRepository.save(taskToUpdate);
        return savedTask;
    }

    private User getCurrentUser() {
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> {
            log.error("Ошибка при получении текущего пользователя. Пользователь не найден в базе данных");
            return new UsernameNotFoundException("Текущий пользователь не найден. Обратитесь в тех.поддержку");
        });
    }

    private User getCurrentAssignee(TaskRequest taskRequest) {
        return userRepository.findByEmail(taskRequest.assignee()).orElseThrow(() -> {
            log.error("Ошибка при добавлении задачи для исполнителя {}. Исполнитель не зарегистрирован", taskRequest.assignee());
            return new NoSuchAssigneeException(String.format(
                    "Ошибка при добавлении задачи для исполнителя %s. Исполнитель не зарегистрирован. Проверьте корректность поля 'исполнитель'", taskRequest.assignee()
            ));
        });
    }

    private Task createNewTask(TaskRequest taskRequest, User currentAdmin, User taskAssignee) {
        Set<Comment> comments = new HashSet<>();
        if (taskRequest.comment() != null && !taskRequest.comment().isEmpty()) {
            comments.add(Comment.builder().message(taskRequest.comment()).date(new Date()).build());
        }
        return Task.builder()
                .title(taskRequest.title())
                .description(taskRequest.description())
                .status(Status.STATUS_PENDING)
                .priority(getPriorityFromTaskRequest(taskRequest))
                .comments(comments)
                .author(currentAdmin)
                .assignee(taskAssignee)
                .date(new Date())
                .build();
    }

    private Priority getPriorityFromTaskRequest(TaskRequest taskRequest) {
        return switch (taskRequest.priority().toUpperCase()) {
            case "MEDIUM" -> Priority.PRIORITY_MEDIUM;
            case "HIGH" -> Priority.PRIORITY_HIGH;
            default -> Priority.PRIORITY_LOW;
        };
    }

    private Status getStatusFromTaskRequest(TaskRequest taskRequest) {
        return switch (taskRequest.status().toUpperCase()) {
            case "IN PROGRESS" -> Status.STATUS_IN_PROGRESS;
            case "DONE" -> Status.STATUS_DONE;
            default -> Status.STATUS_PENDING;
        };
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }
}
