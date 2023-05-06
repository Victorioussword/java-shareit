package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.user.dto.Create;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestInputDto {

    @NotBlank(groups = Create.class)
    private String description;
}