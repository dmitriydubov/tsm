package com.example.tms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

@Schema(description = "Сущность запроса на авторизацию/аутентификацию")
public record SigninRequest(

        @Email(message = "Неправильно введен E-mail. Формат ввода xx@xx.xx")
        @NotEmpty(message = "Поле ввода E-mail не должно быть пустым")
        @Length(min = 3, max = 24, message = "E-mail должен быть не менее 3 и не более 24 символов")
        @Schema(description = "E-mail пользователя", example = "admin1@email.com")
        String email,

        @NotEmpty(message = "Поле ввода не должно быть пустым")
        @Length(min = 4, max = 24, message = "Пароль должен быть не менее 4 и не более 24 символов")
        @Schema(description = "Пароль пользователя", example = "pass")
        String password
) {
}
