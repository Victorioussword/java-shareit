package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.user.dto.Create;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class RequestInputDto {

    private long id;

    @NotNull(groups = Create.class)
    private String description;

    private long requester;

    private LocalDateTime created;
}