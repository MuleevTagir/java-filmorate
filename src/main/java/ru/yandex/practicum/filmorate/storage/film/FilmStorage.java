package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film addFilm(Film film) throws NotFoundException;

    Film updateFilm(Film film) throws NotFoundException;

    Optional<Film> getFilmById(int id) throws NotFoundException;

    List<Film> getAllFilms() throws NotFoundException;

    List<Film> getPopularFilms(int count) throws NotFoundException;

    void addLikeToFilm(int filmId, int userId);
}
