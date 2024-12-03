package com.example.tms.dto;

import com.example.tms.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Schema(description = "Сущность запроса на регистрацию")
public record SignupRequest(

        @Email(message = "Неправильно введен E-mail. Формат ввода xx@xx.xx")
        @NotEmpty(message = "Поле ввода E-mail не должно быть пустым")
        @Length(min = 3, max = 24, message = "E-mail должен быть не менее 3 и не более 24 символов")
        @Schema(description = "E-mail пользователя", example = "admin1@email.com")
        String email,

        @NotEmpty(message = "Поле ввода не должно быть пустым")
        @Length(max = 24, message = "Имя пользователя должно быть не более 24 символов")
        @Schema(description = "Имя пользователя", example = "username")
        String username,

        @NotEmpty(message = "Поле ввода не должно быть пустым")
        @Length(min = 4, max = 24, message = "Пароль должен быть не менее 4 и не более 24 символов")
        @Schema(description = "Пароль пользователя", example = "pass")
        String password,

        @NotEmpty(message = "Поле ввода не должно быть пустым")
        @Schema(description = "Список ролей пользователя", example = "[\"ROLE_ADMIN\"]")
        Set<Role> roles
) {
}
