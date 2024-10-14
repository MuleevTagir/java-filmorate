package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private int id = 0;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film add(Film film) {
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

    public Optional<Film> getFilmById(int id) {
        log.info("Получить фильм id: {}.", id);
        return filmStorage.getFilmById(id);
    }

    public List<Film> getAll() {
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

    public List<Film> getPopularFilms(int count) {
        log.info("Получить топ-{} фильмов.", count);
        return filmStorage.getPopularFilms(count);
    }

    public boolean existsById(int filmId) {
        return filmStorage.getFilmById(filmId).isPresent();
    }

    private int getNextId() {
        return ++id;
    }
}
