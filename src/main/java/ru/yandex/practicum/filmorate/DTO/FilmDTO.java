package ru.yandex.practicum.filmorate.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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
public class FilmDTO {

	Integer id;
	@NotNull
	@NotBlank
	String name;
	@NotNull
	@Size(min = 1, max = 200)
	@EqualsAndHashCode.Exclude
	String description;
	@NotNull
	@Past
	@EqualsAndHashCode.Exclude
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate releaseDate;
	@NotNull
	@EqualsAndHashCode.Exclude

	@Positive
	Integer duration;

}

