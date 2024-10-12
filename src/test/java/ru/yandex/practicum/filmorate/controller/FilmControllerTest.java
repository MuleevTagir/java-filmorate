package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

public class FilmControllerTest {

    private FilmService filmService;

    private Film film;

    @BeforeEach
    void init() {
        this.filmService = new FilmService();
        this.film = new Film();

        film.setName("Крестный отец");
        film.setDescription("Криминал");
        film.setReleaseDate(LocalDate.of(1972, 1, 1));
        film.setDuration(180);
    }

    @Test
    void validateFilmWithCorrectDate() {
        filmService.add(film);
        Assertions.assertEquals(1, filmService.getAll().size(), "разрмер getAll должно быть 1");
    }

    @Test
    void notFoundExceptionTest() {
        this.filmService.add(film);
        film.setId(2);

        Assertions.assertThrows(NotFoundException.class, () -> {
            filmService.update(film);
        }, "Должно быть выброшено исключения из-за отсутсвия Film с id 2");
    }
}
