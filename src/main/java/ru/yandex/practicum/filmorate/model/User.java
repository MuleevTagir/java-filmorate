package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
public class User {
    @NotNull
    @Positive
    private int id;

    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Email должен быть реальным")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелы")
    private String login;

    private String name;

    @NotNull
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    @AssertTrue(message = "Имя для отображения будет изменено на логин, если оно пустое")
    private boolean isNameValid() {
        if (this.name == null || this.name.isEmpty()) {
            this.name = this.login;
        }
        return true;
    }
}
