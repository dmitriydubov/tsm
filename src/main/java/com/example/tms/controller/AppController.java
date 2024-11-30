package com.example.tms.controller;

import com.example.tms.dto.TaskRemoveRequest;
import com.example.tms.dto.TaskRequest;
import com.example.tms.dto.TaskResponse;
import com.example.tms.dto.ViewTaskResponse;
import com.example.tms.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/app")
@RequiredArgsConstructor
public class AppController {

    private final TaskService taskService;

    @PostMapping("/add-task")
    public ResponseEntity<TaskResponse> addNewTask(@RequestBody TaskRequest taskRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(taskRequest));
    }

    @PostMapping("/remove-task")
    public ResponseEntity<TaskResponse> removeTask(@RequestBody TaskRemoveRequest taskRemoveRequest) {
        return ResponseEntity.ok(taskService.removeTask(taskRemoveRequest));
    }

    @PostMapping("/update-task")
    public ResponseEntity<TaskResponse> updateTask(@RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.updateTask(taskRequest));
    }

    @GetMapping("/get-task")
    public ResponseEntity<ViewTaskResponse> getTask(@RequestParam String taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @GetMapping("/get-assignee-tasks")
    public ResponseEntity<Page<ViewTaskResponse>> getUserTasks(
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "10") String size
    ) {
        return ResponseEntity.ok(taskService.getAssigneeTask(userId, page, size));
    }
}
