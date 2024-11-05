package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@Validated
public class User {
	@NotNull
	@NotEmpty
	Integer id;
	@NotNull
	@Email
	String email;
	@NotNull
	String login;
	@NotNull
	@EqualsAndHashCode.Exclude
	String name;
	@NotNull
	@EqualsAndHashCode.Exclude
	LocalDate birthday;
}
