package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private int id = 0;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film add(Film film) {
        log.info("Добавление фильма: {}.", film);
        film.setId(this.getNextId());
        return filmStorage.addFilm(film);
    }

    public Film update(Film film) {
        log.info("Обновление фильма: {}.", film);
        filmStorage.getFilmById(film.getId());
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(int id) {
        log.info("Получить фильм id: {}.", id);
        return filmStorage.getFilmById(id);
    }

    public List<Film> getAll() {
        log.info("Получить все фильмы.");
        return filmStorage.getAllFilms();
    }

    public void addLike(int filmId, int userId) {
        log.info("Пользователь id= {} поставил like фильму id={}.", userId, filmId);
        Film film = filmStorage.getFilmById(filmId);
        film.getLikes().add(userId);
        filmStorage.updateFilm(film);
    }

    public void removeLike(int filmId, int userId) {
        log.info("Пользователь id= {} убрал like фильму id={}.", userId, filmId);
        Film film = filmStorage.getFilmById(filmId);
        film.getLikes().remove(userId);
        filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Получить топ-{} фильмов.", count);
        return filmStorage.getPopularFilms(count);
    }

    private int getNextId() {
        return ++id;
    }
}
