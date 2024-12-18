package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RATINGS")
public class Rating {
    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

}