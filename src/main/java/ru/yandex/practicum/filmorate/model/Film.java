package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.Service.DurationDeserializer;
import static java.time.LocalDate.*;

/**
 * Film.
 */
@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Validated
public class Film {
	private static final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);
	@NotNull
	@NotBlank
	@EqualsAndHashCode.Exclude
	Integer id;
	@NotNull
	@NotBlank
	String name;
	@NotNull
	@Size(min = 1, max = 200)
	@Pattern(regexp = "^[a-zA-Zа-яА-ЯёЁ\\s]+$")
	@EqualsAndHashCode.Exclude
	String description;
	@NotNull
	@Past
	@EqualsAndHashCode.Exclude
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate releaseDate;
	@NotNull
	@EqualsAndHashCode.Exclude
	@JsonDeserialize(using = DurationDeserializer.class)
	Duration duration;
	
	@AssertTrue(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
	public boolean isValidReleaseDate() {
		return !releaseDate.isBefore(LocalDate.of(1895, 12, 28));
	}
}
