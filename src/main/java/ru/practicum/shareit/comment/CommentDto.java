package ru.practicum.shareit.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {

    private Long id;


    @NotBlank(groups = Create.class)
    private String text;
    private Long authorId;
    private String authorName;
    private LocalDateTime created;
}
