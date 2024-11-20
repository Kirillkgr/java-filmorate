package ru.yandex.practicum.filmorate.DTO;


import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class FilmDto {
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
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate releaseDate;

	@NotNull
	@EqualsAndHashCode.Exclude
	@Positive
	Integer duration;

	@AssertTrue(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
	public boolean isValidReleaseDate() {
		return !releaseDate.isBefore(LocalDate.of(1895, 12, 28));
	}
}

