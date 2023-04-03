package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;


    public UserDto getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("User не обнружен");
        }
        UserDto userDtoForReturn = UserMapper.toUserDto(user.get());
        log.info("UserService - getById(). Возвращен {}", userDtoForReturn.toString());
        return userDtoForReturn;
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        UserDto user = UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
        log.info("UserService - createUser(). ДОбавлен {}", user.toString());
        return user;
    }

    @Transactional
    public UserDto update(UserDto userDto, long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("UserService - update().User {} для обновления не существует", id);
            throw new NotFoundException("UserService - update().User для обновления не существует");
        });
        userDto.setId(id);
        prepareUserForUpdate(user, userDto);
        UserDto userDtoForReturn = UserMapper.toUserDto(userRepository.save(user));
        log.info("UserService - update(). Обновлен {}", userDtoForReturn.toString());
        return userDtoForReturn;
    }

    @Transactional
    public void delById(long id) {
        checkUnicId(id);
        log.info("UserService - delById(). Удален пользователь с id {}", id);
        userRepository.deleteById(id);
    }


    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
        log.info("UserController - getAll(). Возвращен список из {} пользователей", userDtos.size());
        return userDtos;
    }


    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        log.info("UserController - findByEmail(). Возвращен  {}", user);
        return user;
    }


    private void checkUnicId(long id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new ValidationException("Пользователь с таким id не существует");
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
    }
}
