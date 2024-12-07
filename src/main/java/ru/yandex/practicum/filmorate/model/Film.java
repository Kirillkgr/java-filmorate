package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
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

	Set<Integer> likesIds;

	@NotNull
	@Size(min = 1, message = "Фильм должен иметь хотя бы один жанр.")
	Set<String> genres = new HashSet<>();

	@NotNull(message = "Рейтинг не может быть пустым.")
	MPARating mpaRating;

	@AssertTrue(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
	public boolean isValidReleaseDate() {
		return !releaseDate.isBefore(RELEASE_DATE);
	}

	public Set<Integer> getLikeUsers() {
		if (likesIds == null) {
			likesIds = new HashSet<>();
		}
		return likesIds;
	}

	public enum MPARating {
		G, PG, PG_13, R, NC_17
	}
}
