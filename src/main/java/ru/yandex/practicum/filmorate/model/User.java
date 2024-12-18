package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class User {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Size(max = 255)
    @NotNull
    @Email
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Size(max = 50)
    @NotNull
    @Column(name = "LOGIN", nullable = false, length = 50)
    private String login;

    @NotNull
    @EqualsAndHashCode.Exclude
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "BIRTHDAY", nullable = false)
    private LocalDate birthday;

}