package ru.yandex.practicum.filmorate.DTO;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
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

	Integer id;

	@NotNull(message = "Film name cannot be null")
	private String name;

	@NotNull(message = "Description cannot be null")
	private String description;

	@NotNull(message = "Release date cannot be null")
	@FutureOrPresent(message = "Release date must not be in the future")
	private LocalDate releaseDate;

	@NotNull(message = "Duration cannot be null")
	private Integer duration;

	public FilmDto(String errorMessage) {
		this.name = errorMessage;
	}
}

