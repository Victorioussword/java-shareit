package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.dto.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;



@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoInput {


    private Long id;

    @NotBlank(groups = Create.class)
    private String text;

    private Long authorId;

    private String authorName;

    private LocalDateTime created;
}
