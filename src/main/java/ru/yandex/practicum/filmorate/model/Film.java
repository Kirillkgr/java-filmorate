package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalDate;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
@Validated
public class Film {
    private static final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);

    Integer id;

    @NotNull
    @NotBlank
    String name;

    @NotNull
    @Size(min = 1, max = 200)
    @EqualsAndHashCode.Exclude
    String description;

    @NotNull
    @PastOrPresent(message = "Дата выхода должна быть в прошлом или настоящем.")
    @AssertTrue(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    @EqualsAndHashCode.Exclude
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @NotNull
    @EqualsAndHashCode.Exclude
    Duration duration;

    @JsonProperty("mpa")
    @EqualsAndHashCode.Exclude
    private RatingMpa rating;

    @EqualsAndHashCode.Exclude
    private LinkedHashSet<GenreModel> genres;

    Set<Integer> likesIds;

    public Film(int newId, @NotNull @NotBlank String name,
                @NotNull @Size(min = 1, max = 200) String description,
                @NotNull @PastOrPresent(message = "Дата выхода должна быть в прошлом или настоящем.") LocalDate releaseDate,
                Duration duration,
                LinkedHashSet<GenreModel> genre) {
        this.id = newId;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = genre;

    }


    @AssertTrue(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    public boolean isValidReleaseDate() {
        return !releaseDate.isBefore(LocalDate.of(1895, 12, 28));
    }

    public Set<Integer> getLikeUsers() {
        if (likesIds == null) {
            likesIds = new HashSet<>();
        }
        return likesIds;
    }

}
