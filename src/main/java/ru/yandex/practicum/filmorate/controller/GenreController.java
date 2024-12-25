package ru.yandex.practicum.filmorate.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenresRepository;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreController {
    GenresRepository genresRepository;

    @GetMapping("/genres")
    public Set<Genre> getAllGenres() {
        return genresRepository.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public ResponseEntity<Genre> getByIdGenre(@PathVariable Integer id) {
        Genre actualGenre = genresRepository.getGenre(id);
        if (actualGenre == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(actualGenre);
    }
}