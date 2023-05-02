package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserShort;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMapperTest {


    @Test
    void toUserDtoTest() {
        User user = new User(1L, "userName", "user@mail.ru");
        UserDto userDto = UserMapper.toUserDto(user);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void toUserTest() {
        UserDto userDto = new UserDto(1L, "userName", "user@mail.ru");
        User user = UserMapper.toUser(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void toUserShortTest() {
        User user = new User(1L, "userName", "user@mail.ru");
        UserShort userShort = UserMapper.toUserShort(user);
        assertEquals(user.getId(), userShort.getId());
    }
}