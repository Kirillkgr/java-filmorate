package ru.yandex.practicum.filmorate.Enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@ToString
@Getter
@AllArgsConstructor
public enum Genre {
    COMEDY(1, "Комедия"), DRAMA(2, "Драма"), CARTOON(3, "Мультфильм"), THRILLER(4, "Триллер"), DOCUMENTARY(5, "Документальный"), ACTION(6, "Боевик");

    private final int id;

    private final String name;

    @JsonCreator
    public static Genre forValues(@JsonProperty("id") int id) {
        for (Genre genre : Genre.values()) {
            if (genre.id == id) {
                return genre;
            }
        }
        return null;
    }
}
