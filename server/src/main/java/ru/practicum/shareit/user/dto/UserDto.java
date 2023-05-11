package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@AllArgsConstructor
@Setter
@ToString
public class UserDto {

    private long id;
    private String name;
    private String email;
}