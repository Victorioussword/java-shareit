package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;


    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.info("UserController - createUser(). Создан {}", userDto.toString());
        return userService.createUser(userDto);
    }


    @PatchMapping("/{id}")
    public UserDto update(@PathVariable long id,  @RequestBody UserDto userDto) {
        userDto.setId(id);
        log.info("UserController - update(). Обновлен {}", userDto.toString());
        return userService.update(userDto, id);
    }


    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable long userId) {
        UserDto userDtoForReturn = userService.getById(userId);
        log.info("UserController - getUser(). Возвращен {}", userDtoForReturn.toString());
        return userDtoForReturn;
    }

    @GetMapping
    public List<UserDto> getAll(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                @RequestParam(name = "size", defaultValue = "10") Integer size) {
        List<UserDto> userDtos = userService.getAll(from, size);
        log.info("UserController - getAll(). Возвращен список из {} пользователей", userDtos.size());
        return userDtos;
    }

    @DeleteMapping("/{id}")
    public void delById(@PathVariable Long id) {
        log.info("UserController - delById(). Удален пользователь с id {}", id);
        userService.delById(id);
    }
}
