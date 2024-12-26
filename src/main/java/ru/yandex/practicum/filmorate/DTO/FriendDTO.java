package ru.yandex.practicum.filmorate.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendDTO {
    private Integer id;
    private String name;
    private String status;

    public FriendDTO(int id, String name,String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public FriendDTO() {
    }
}
