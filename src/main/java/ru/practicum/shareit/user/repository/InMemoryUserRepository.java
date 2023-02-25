package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryUserRepository implements  UserRepository{
    private long id = 1;
    private Map<Long, User> users = new HashMap<>();
    private Set<String> usersByEmail = new HashSet<>();


    public User createUser(User user) {
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        usersByEmail.add(user.getEmail());
        log.info("InMemoryUserRepository - createUser(). ДОбавлен {}", user.toString());
        return user;
    }

    public User getById(long id) {
        User userForReturn = users.get(id);
        log.info("InMemoryUserRepository - getById(). Возвращен {}", userForReturn.toString());
        return userForReturn;
    }

    public User update(User user, String oldEmail) {
        usersByEmail.remove(oldEmail);   // удалили по старому Email
        usersByEmail.add(user.getEmail());   // положили по новому Email
        users.put(user.getId(), user);  // положили по ID
        log.info("InMemoryUserRepository - update(). Обновлен {}", user.toString());
        return user;
    }

    public void delById(long id) {
        usersByEmail.remove(users.get(id).getEmail());
        log.info("InMemoryUserRepository - delById(). Удален пользователь с id {}", id);
        users.remove(id);
        return;
    }

    public Set<String> getUsersByEmail() {
        return Set.copyOf(usersByEmail);
    }

    public Map<Long, User> getUsers() {
        return Map.copyOf(users);
    }

    public List<User> getAll() {
        List <User> usersRForReturn= users.values().stream().collect(Collectors.toList());
        return usersRForReturn;
    }
}