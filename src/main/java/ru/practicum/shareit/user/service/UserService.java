package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserRepository userRepository;


    public User getById(long id) {
        User userForReturn = userRepository.getById(id);
        log.info("UserService - getById(). Возвращен {}", userForReturn.toString());
        return userRepository.getById(id);
    }

    public User createUser(User user) {
        if (userRepository.getUsersByEmail().contains(user.getEmail())) {
            throw new ValidationException("Создание - Укажите корректную почту gh - EMAIL");
        }
        log.info("UserService - createUser(). ДОбавлен {}", user.toString());
        return userRepository.createUser(user);
    }

    public User update(UserDto userDto, long id) {
        User user = userRepository.getById(id);  // получили юзера для обновления

        String oldEmail = user.getEmail();

        userDto.setId(id);
        checkUnicId(userRepository.getById(id));  // проверили id - есть
        catchUnunicEmail(userDto, user);
        prepareUserForUpdate(user, userDto);
        userRepository.update(user, oldEmail);
        log.info("UserService - update(). Обновлен {}", user.toString());
        return user;
    }

    private void catchUnunicEmail(UserDto newUserDto, User oldUser) {
        if (newUserDto.getEmail() != null && !oldUser.getEmail().equals(newUserDto.getEmail()) && userRepository.getUsersByEmail().contains(newUserDto.getEmail())) {
            throw new ValidationException("Укажите корректную почту - EMAIL");
        }
    }

    private User prepareUserForUpdate(User user, UserDto userDto) {
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        log.info("UserService - Было {} , Стало {}", user.toString(), userDto.toString());
        return new User(user.getId(), user.getName(), user.getEmail());
    }

    private void checkUnicId(User user) {
        if (!userRepository.getUsers().containsKey(user.getId())) {
            throw new ValidationException("Пользователь с таким id не существует");
        }
    }

    public void delById(long id) {
        checkUnicId(userRepository.getById(id));
        log.info("UserService - delById(). Удален пользователь с id {}", id);
        userRepository.delById(id);
    }

    public List<User> getAll() {
        List<User> users = userRepository.getAll();
        log.info("UserController - getAll(). Возвращен список из {} пользователей", users.size());
        return users;
    }
}
