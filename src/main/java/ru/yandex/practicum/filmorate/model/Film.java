package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
@NoArgsConstructor
@Table(name = "FILMS")
public class Film {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "DESCRIPTION", nullable = false, length = 200)
    private String description;

    @NotNull
    @PastOrPresent(message = "Дата выхода должна быть в прошлом или настоящем.")
    @AssertTrue(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    @EqualsAndHashCode.Exclude
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "RELEASE_DATE", nullable = false)
    private LocalDate releaseDate;

    @NotNull
    @EqualsAndHashCode.Exclude
    @Column(name = "DURATION", nullable = false)
    private Integer duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "RATING_ID")
    private Rating rating;

}