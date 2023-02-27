package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserRepository {

    UserDto createUser(UserDto userDto);

    User getById(long id);

    User update(User user, String oldEmail);

    void delById(long id);

    Set<String> getUsersByEmail();

    Map<Long, User> getUsers();

    List<User> getAll();
}
