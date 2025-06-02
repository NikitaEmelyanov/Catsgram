package ru.yandex.practicum.catsgram.model;

import java.time.Instant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class Post {
    private Long id;
    private Long authorId;
    private String description;
    private Instant postDate;


}
