package com.example.tms.controller;

import com.example.tms.dto.TaskRemoveRequest;
import com.example.tms.dto.TaskRequest;
import com.example.tms.dto.TaskResponse;
import com.example.tms.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
