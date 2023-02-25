package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Map;
import java.util.Set;

public interface UserRepository {

    User createUser(User user);

    User getById(long id);

    User update(User user, String oldEmail);

    void delById(long id);

    Set<String> getUsersByEmail();

    Map<Long, User> getUsers();
}
