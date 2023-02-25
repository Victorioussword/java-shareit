package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private  final  UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("UserController - createUser(). Создан {}", user.toString());
        return userService.createUser(user);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable long userId) {
        User userForReturn = userService.getById(userId);
        log.info("UserController - getUser(). Возвращен {}", userForReturn.toString());
        return userForReturn;
    }

    @GetMapping
    public List<User> getAll(){
        List<User> users = userService.getAll();
        log.info("UserController - getAll(). Возвращен список из {} пользователей" , users.size());
        return users;
    }

    @PatchMapping("/{id}")
    public User update(@PathVariable long id, @Valid @RequestBody UserDto userDto) {
        userDto.setId(id);
        log.info("UserController - update(). Обновлен {}", userDto.toString());
        return userService.update(userDto, id);
    }

    @DeleteMapping("/{id}")
    public void delById(@PathVariable long id) {
        log.info("UserController - delById(). Удален пользователь с id {}", id);
        userService.delById(id);
        return;
    }
}
