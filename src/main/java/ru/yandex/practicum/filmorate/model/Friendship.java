package ru.yandex.practicum.filmorate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "FRIENDSHIP", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"USER_ID", "FRIEND_ID"})
})
public class Friendship {

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "FRIEND_ID")
    private User friend;

    @Size(max = 50)
    @NotNull
    @Column(name = "STATUS", nullable = false, length = 50)
    private String status;
}