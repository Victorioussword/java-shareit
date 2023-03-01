package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public UserDto getById(Long id) {

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
        User user = userRepository.getById(id);
        String oldEmail = user.getEmail();
        userDto.setId(id);

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
        List<UserDto> userDtos = users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
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

    private void prepareUserForUpdate(User user, UserDto userDto) {
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            user.setEmail(userDto.getEmail());
        }
        log.info("UserService - Было {} , Стало {}", user.toString(), userDto.toString());
        return;
    }
}
