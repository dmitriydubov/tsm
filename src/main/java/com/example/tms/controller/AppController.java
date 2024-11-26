package com.example.tms.controller;

import com.example.tms.dto.TaskRequest;
import com.example.tms.dto.TaskResponse;
import com.example.tms.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/app")
@RequiredArgsConstructor
public class AppController {

    private final TaskService taskService;

    @PostMapping("/add-task")
    public ResponseEntity<TaskResponse> addNewTask(@RequestBody TaskRequest taskRequest) {
        return ResponseEntity.ok(taskService.createTask(taskRequest));
    }
}
