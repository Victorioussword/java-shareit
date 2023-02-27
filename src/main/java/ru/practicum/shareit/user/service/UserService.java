package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public UserDto getById(Long id) {
        if (id == null || id == 0) {
            throw new NotFoundException("Не указан Id пользователя для получения");
        }
        UserDto userDtoForReturn = UserMapper.toUserDto(userRepository.getById(id));
        log.info("UserService - getById(). Возвращен {}", userDtoForReturn.toString());
        return userDtoForReturn;
    }

    public UserDto createUser(UserDto userDto) {
        if (userRepository.getUsersByEmail().contains(userDto.getEmail())) {
            throw new ValidationException("Создание - Почта занята - EMAIL");
        }
        log.info("UserService - createUser(). ДОбавлен {}", userDto.toString());
        return userRepository.createUser(userDto);
    }

    public UserDto update(UserDto userDto, long id) {
        User user = userRepository.getById(id);  // получили юзера для обновления
        String oldEmail = user.getEmail();
        userDto.setId(id);
        checkUnicId(id);  // проверили id - есть
        catchUnunicEmail(userDto, user);
        prepareUserForUpdate(user, userDto);
        UserDto userDtoForReturn = UserMapper.toUserDto(userRepository.update(user, oldEmail));
        log.info("UserService - update(). Обновлен {}", userDtoForReturn.toString());
        return userDtoForReturn;
    }

    public void delById(long id) {
        checkUnicId(id);
        log.info("UserService - delById(). Удален пользователь с id {}", id);
        userRepository.delById(id);
    }

    public List<UserDto> getAll() {
        List<User> users = userRepository.getAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            userDtos.add(UserMapper.toUserDto(users.get(i)));
        }
        log.info("UserController - getAll(). Возвращен список из {} пользователей", userDtos.size());
        return userDtos;
    }

    private void checkUnicId(long id) {
        if (!userRepository.getUsers().containsKey(id)) {
            throw new ValidationException("Пользователь с таким id не существует");
        }
    }

    private void catchUnunicEmail(UserDto newUserDto, User oldUser) {
        if (newUserDto.getEmail() != null && !oldUser.getEmail().equals(newUserDto.getEmail()) && userRepository.getUsersByEmail().contains(newUserDto.getEmail())) {
            throw new ValidationException("Укажите корректную почту - EMAIL");
        }
    }

    private User prepareUserForUpdate(User user, UserDto userDto) {
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            user.setEmail(userDto.getEmail());
        }
        log.info("UserService - Было {} , Стало {}", user.toString(), userDto.toString());
        return new User(user.getId(), user.getName(), user.getEmail());
    }

}
