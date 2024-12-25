package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
public class RatingMpaModel {

    private Long id;

    private String name;
    private String description;
}
