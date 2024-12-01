package com.example.tms.service.impl;

import com.example.tms.dto.*;
import com.example.tms.error.NoSuchAssigneeException;
import com.example.tms.error.NoSuchTaskMaintainer;
import com.example.tms.model.*;
import com.example.tms.repository.CommentsRepository;
import com.example.tms.repository.TaskRepository;
import com.example.tms.repository.UserRepository;
import com.example.tms.service.TaskService;
import com.example.tms.service.ValidationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.text.SimpleDateFormat;
import java.util.*;

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
        validationService.validateIsTaskExist(Long.valueOf(taskRequest.taskId()));

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

    @Override
    public ViewTaskResponse getTask(
            @Valid @NotEmpty @Pattern(regexp = "\\d+", message = "id задачи должен состоять из целого числа")
            String taskId
    ) {
        var task = validationService.validateByExistingTaskAndGet(Long.parseLong(taskId));
        List<CommentDTO> commentDTOList = createCommentDTOList(task);

        return createViewTaskResponse(task, commentDTOList);
    }

    @Override
    public Page<ViewTaskResponse> getUserTasks(String userId, String taskRole, String page, String size) {
        var currentAdmin = getCurrentUser();
        validationService.validateByAdminRole(currentAdmin);

        Pageable pageable = PageRequest.of(
                Integer.parseInt(page), Integer.parseInt(size), Sort.by(Sort.Direction.ASC, "id")
        );

        switch (taskRole) {
            case "author" -> {
                return getAuthorTasks(Long.parseLong(userId), pageable);
            }
            case "assignee" -> {
                return getAssigneeTasks(Long.parseLong(userId), pageable);
            }
            default -> {
                log.error("Ошибка получения списка задач пользователя {}, роль пользователя {}.", userId, taskRole);
                throw new NoSuchTaskMaintainer("Ошибка в указании роли пользователя в задаче. Обратитесь в тех.поддержку");
            }
        }
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

    private Page<ViewTaskResponse> getAssigneeTasks(long userId, Pageable pageable) {
        return taskRepository.findByAssigneeId(userId, pageable).map(task -> {
            List<CommentDTO> commentDTOList = createCommentDTOList(task);

            return createViewTaskResponse(task, commentDTOList);
        });
    }

    private Page<ViewTaskResponse> getAuthorTasks(long userId, Pageable pageable) {
        return taskRepository.findByAuthorId(userId, pageable).map(task -> {
            List<CommentDTO> commentDTOList = createCommentDTOList(task);

            return createViewTaskResponse(task, commentDTOList);
        });
    }

    private List<CommentDTO> createCommentDTOList(Task task) {
        return task.getComments().stream()
                .map(comment -> new CommentDTO(
                        String.valueOf(comment.getId()),
                        comment.getMessage(),
                        formatDate(comment.getDate()))
                )
                .toList();
    }

    private ViewTaskResponse createViewTaskResponse(Task task, List<CommentDTO> commentDTOList) {
        return new ViewTaskResponse(
                String.valueOf(task.getId()),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().toString(),
                task.getPriority().toString(),
                new ArrayList<>() {{
                    addAll(commentDTOList);
                }},
                task.getAuthor().getEmail(),
                task.getAssignee().getEmail(),
                formatDate(task.getDate())
        );
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
