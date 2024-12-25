package ru.yandex.practicum.filmorate.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

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

    @Valid
    @NotNull
    Integer id;

    String name;

    public Genre toGenre() {
        return Genre.forValues(this.id);
    }
}
