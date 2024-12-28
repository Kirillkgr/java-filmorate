package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.Enums.Genre;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
public class GenreModel {

    @NotNull
    Integer id;

    String name;

    public Genre toGenre() {
        return Genre.forValues(this.id);
    }
}
