package ru.yandex.practicum.filmorate.controller;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.Enums.RatingMpa;
import ru.yandex.practicum.filmorate.repository.RatingRepository;

import java.util.List;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/mpa")
public class MpaController {

    RatingRepository ratingRepository;

    @GetMapping
    public ResponseEntity<List<RatingMpa>> getAllMpa() {
        List<RatingMpa> ratingMpa = ratingRepository.findAll();
        if (ratingMpa == null || ratingMpa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ratingMpa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingMpa> getMpaById(@Validated @PathVariable Integer id) {
        RatingMpa ratingMpa = ratingRepository.findById(id);
        if (ratingMpa == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ratingMpa);
    }
}
