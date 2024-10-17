package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreStorage genreStorage;
    private final MpaRatingStorage mpaRatingStorage;

    private int id = 0;

    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       UserService  userService,
                       @Qualifier("GenreDbStorage") GenreStorage genreStorage,
                       @Qualifier("MpaRatingDbStorage") MpaRatingStorage mpaRatingStorage
    ) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreStorage = genreStorage;
        this.mpaRatingStorage = mpaRatingStorage;
    }

    public Film add(Film film) throws NotFoundException {
        log.info("Добавление фильма: {}.", film);
        film.setId(this.getNextId());
        validateFilmWithDependencies(film);
        return filmStorage.addFilm(film);
    }

    public Film update(Film film) throws NotFoundException {
        log.info("Обновление фильма: {}.", film);
        validateFilmWithDependencies(film);
        filmStorage.getFilmById(film.getId())
                .orElseThrow(() -> new NotFoundException("Фильм с таким id не найден"));
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) throws NotFoundException {
        log.info("Получить фильм id: {}.", id);
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с таким id не найден"));
    }

    public List<Film> getAll() throws NotFoundException {
        log.info("Получить все фильмы.");
        return filmStorage.getAllFilms();
    }

    public void addLike(int filmId, int userId) throws NotFoundException {
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
        if (!userService.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        filmStorage.addLikeToFilm(filmId, userId);
    }

    public void removeLike(int filmId, int userId) throws NotFoundException {
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
        if (!userService.existsById(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        film.getLikes().remove(userId);
        filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilms(int count) throws NotFoundException {
        return filmStorage.getPopularFilms(count);
    }

    public boolean existsById(int filmId) throws NotFoundException {
        return filmStorage.getFilmById(filmId).isPresent();
    }

    private void validateFilmWithDependencies(Film film) {
        if (film.getMpa() == null || film.getMpa().getId() == 0) {
            throw new ValidationException("MPA рейтинг не может быть пустым");
        }
        if (!mpaRatingStorage.getMpaById(film.getMpa().getId()).isPresent()) {
            throw new ValidationException("MPA рейтинг с id " + film.getMpa().getId() + " не найден");
        }

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                if (!genreStorage.getGenreById(genre.getId()).isPresent()) {
                    throw new ValidationException("Жанр с id " + genre.getId() + " не найден");
                }
            }
        }
    }

    private int getNextId() {
        return ++id;
    }
}
