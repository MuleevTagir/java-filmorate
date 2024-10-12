package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
public class Film {

    @Getter(AccessLevel.NONE)
    private final LocalDate filmBeginReleaseDate = LocalDate.of(1895, 12, 18);

    @NotNull
    private int id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Длина описания фильма не может превышать 200 символов")
    private String description;

    @NotNull(message = "Дата релиза фильма не может быть пустой")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;

    @AssertTrue(message = "Дата релиза фильма должна быть после 18.12.1895")
    public boolean isValidReleaseDate() {
        return releaseDate.isAfter(filmBeginReleaseDate);
    }
}
