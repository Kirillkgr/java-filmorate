package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
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
public class User {

	Integer id;

	@NotNull
	@Email
	String email;

	@NotNull
	String login;

	String name;

	@NotNull
	@EqualsAndHashCode.Exclude
	@Past
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate birthday;

	@ToString.Exclude
	Set<Integer> friendsIds;

	public Set<Integer> getFriends() {
		if (friendsIds == null) {
			friendsIds = new HashSet<>();
		}
		return friendsIds;
	}
}
