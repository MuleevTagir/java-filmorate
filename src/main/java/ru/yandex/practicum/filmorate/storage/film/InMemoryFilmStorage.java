package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final List<Film> films = new ArrayList<>();

    @Override
    public Film addFilm(Film film) {
        films.add(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId() == film.getId()) {
                films.set(i, film);
                return film;
            }
        }
        return null;
    }

    @Override
    public Film getFilmById(int id) {
        return films.stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(
                        () -> new NotFoundException("Фильм id=" + id + " не найден.")
                );
    }

    @Override
    public List<Film> getAllFilms() {
        return films;
    }
}
