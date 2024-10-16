package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaRatingStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private int id = 0;

    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film add(Film film) throws NotFoundException {
        log.info("Добавление фильма: {}.", film);
        film.setId(this.getNextId());
        return filmStorage.addFilm(film);
    }

    public Film update(Film film) throws NotFoundException {
        log.info("Обновление фильма: {}.", film);
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
        if (userStorage.getUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
    }

    public void removeLike(int filmId, int userId) throws NotFoundException {
        Film film = filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден"));
        if (userStorage.getUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        film.getLikes().remove(userId);
        filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilms(int count) throws NotFoundException {
        log.info("Получить топ-{} фильмов.", count);
        return filmStorage.getPopularFilms(count);
    }

    public boolean existsById(int filmId) throws NotFoundException {
        return filmStorage.getFilmById(filmId).isPresent();
    }

    private int getNextId() {
        return ++id;
    }
}
