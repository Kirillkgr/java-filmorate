package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "GENRES")
public class Genre {
    @Id
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;
}