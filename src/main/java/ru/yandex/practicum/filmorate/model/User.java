package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    Integer id;

    @NotNull
    @Email
    String email;

    @NotNull
    @NotBlank
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
