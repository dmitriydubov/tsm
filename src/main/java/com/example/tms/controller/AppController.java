package com.example.tms.controller;

import com.example.tms.dto.TaskRemoveRequest;
import com.example.tms.dto.TaskRequest;
import com.example.tms.dto.TaskResponse;
import com.example.tms.dto.ViewTaskResponse;
import com.example.tms.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/app")
@RequiredArgsConstructor
@Tag(name = "Основной контроллер", description = "Контроллер для добавления/удаления/изменения/получения задач пользователей")
public class AppController {

    private final TaskService taskService;

    @GetMapping("/test")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Тестовый запрос", description = "Позволяет проверить работоспособность сервера")
    public ResponseEntity<String> test(@RequestParam @Parameter(description = "Сообщение серверу", required = true, example = "ping") String ping) {
        return ResponseEntity.ok(taskService.checkServerConnection(ping));
    }

    @PostMapping("/add-task")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запрос на добавление новой задачи", description = "Позволяет отправить запрос на добавление новой задачи. Создать новую задачу может только пользователь с уровнем доступа admin")
    public ResponseEntity<TaskResponse> addNewTask(@RequestBody TaskRequest taskRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskRequest));
    }

    @PostMapping("/remove-task")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запрос на добавление новой задачи", description = "Позволяет отправить запрос на удаление задачи. Удалить задачу может только пользователь с уровнем доступа admin")
    public ResponseEntity<TaskResponse> removeTask(@RequestBody TaskRemoveRequest taskRemoveRequest) {
        return ResponseEntity.ok(taskService.removeTask(taskRemoveRequest));
    }

    @PostMapping("/update-task")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запрос на обновление существующей задачи", description = "Позволяет отправить запрос на обновление задачи. Пользователь с уровнем доступа admin может менять приоритет, статус, заголовок, описание задачи, а также менять исполнителя и оставлять комментарии. Обычный пользователь может только оставлять комментарии и менять статус задачи.")
    public ResponseEntity<TaskResponse> updateTask(@RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.updateTask(taskRequest));
    }

    @GetMapping("/get-task")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запрос на получение существующей задачи пользователя", description = "Позволяет получить задачу пользователя по id задачи.")
    public ResponseEntity<ViewTaskResponse> getTask(@RequestParam @Parameter(description = "Уникальный id задачи", required = true, example = "1") String taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @GetMapping("/get-user-tasks")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Запрос на получение всех задач пользователя", description = "Позволяет получить задачи пользователя по id пользователя и роли автор/исполнитель.")
    public ResponseEntity<Page<ViewTaskResponse>> getUserTasks(
            @RequestParam @Parameter(description = "Уникальный id пользователя", required = true, example = "2") String userId,
            @RequestParam @Parameter(description = "Роль пользователя в задаче", required = true, example = "assignee") String taskRole,
            @RequestParam(defaultValue = "0") @Parameter(description = "Значение постраничного вывода", example = "0") String page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Значение количества выводимых задач", example = "10") String size
    ) {
        return ResponseEntity.ok(taskService.getUserTasks(userId, taskRole, page, size));
    }
}
