package ru.practicum.shareit.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDtoOutput {

    private Long id;
    private String text;
    private Long authorId;
    private String authorName;
    private LocalDateTime created;
}
