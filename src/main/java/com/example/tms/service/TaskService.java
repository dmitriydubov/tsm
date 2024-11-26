package com.example.tms.service;

import com.example.tms.dto.TaskRequest;
import com.example.tms.dto.TaskResponse;

public interface TaskService {

    TaskResponse createTask(TaskRequest taskRequest);
}
