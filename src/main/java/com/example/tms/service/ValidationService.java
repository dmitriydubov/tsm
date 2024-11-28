package com.example.tms.service;

import com.example.tms.model.Task;
import com.example.tms.model.User;

public interface ValidationService {

    void validateByExistingEmail(String email);

    void validateByAdminRole(User user);

    Task validateByExistingTaskAndGet(Long taskId);

    void validateTaskByAssignee(User user, Long taskId);
}
