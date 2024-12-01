package com.example.tms.service.impl;

import com.example.tms.error.InvalidUserTaskException;
import com.example.tms.error.IsNotAdminActionException;
import com.example.tms.error.NoSuchTaskException;
import com.example.tms.error.UserAlreadyExistException;
import com.example.tms.model.Role;
import com.example.tms.model.Task;
import com.example.tms.model.User;
import com.example.tms.repository.TaskRepository;
import com.example.tms.repository.UserRepository;
import com.example.tms.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationServiceImpl implements ValidationService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Override
    public void validateByExistingEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            log.info("Не удалось сохранить пользователя {}. Пользователь уже зарегистрирован", email);
            throw new UserAlreadyExistException("Пользователь уже зарегистрирован");
        }
    }

    @Override
    public void validateByAdminRole(User user) {
        if (!user.getRoles().contains(Role.ROLE_ADMIN)) {
            log.error("Попытка пользователя {} создать новую задачу. У пользователя нет прав администратора", user.getEmail());
            throw new IsNotAdminActionException("Только пользователь с уровнем доступа 'администратор' может добавлять новые задачи");
        }
    }

    @Override
    public Task validateByExistingTaskAndGet(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> {
            log.error("Ошибка при добавлении задачи id:{}. Задачи с таким id нет в базе данных", taskId);
            return new NoSuchTaskException("Такой задачи нет");
        });
    }

    @Override
    public void validateIsTaskExist(Long taskId) {
        taskRepository.findById(taskId).ifPresent((task -> {
            log.error("Ошибка при добавлении задачи id:{}. Задачи с таким id уже в базе данных", taskId);
            throw new InvalidUserTaskException(String.format("Задача с id %s уже существует", taskId));
        }));
    }

    @Override
    public void validateTaskByAssignee(User user, Long taskId) {
        var currentTask = taskRepository.findById(taskId).orElseThrow();
        var taskAssignee = currentTask.getAssignee();
        if (!user.equals(taskAssignee)) {
            log.error("Ошибка при редактировании задачи id:{} пользователем {}. Пользователь может редактировать только свои задачи", taskId, user.getEmail());
            throw new InvalidUserTaskException("Пользователь может редактировать только свои задачи");
        }
    }
}
