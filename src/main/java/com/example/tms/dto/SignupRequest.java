package com.example.tms.dto;

import com.example.tms.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

public record SignupRequest(

        @Email(message = "Неправильно введен E-mail. Формат ввода xx@xx.xx")
        @NotEmpty(message = "Поле ввода E-mail не должно быть пустым")
        @Length(min = 3, max = 24, message = "E-mail должен быть не менее 3 и не более 24 символов")
        String email,

        @NotEmpty(message = "Поле ввода не должно быть пустым")
        @Length(max = 24, message = "Имя пользователя должно быть не более 24 символов")
        String username,

        @NotEmpty(message = "Поле ввода не должно быть пустым")
        @Length(min = 4, max = 24, message = "Пароль должен быть не менее 4 и не более 24 символов")
        String password,

        @NotEmpty(message = "Поле ввода не должно быть пустым")
        Set<Role> roles
) {
}
