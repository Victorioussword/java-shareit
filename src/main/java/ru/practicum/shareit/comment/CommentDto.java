package ru.practicum.shareit.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {

    private Long id;

    @NotNull(groups = Create.class)
    @NotBlank(groups = Create.class)
    private String text;
    private Long authorId;
    private String authorName;
    private LocalDateTime created;
}
