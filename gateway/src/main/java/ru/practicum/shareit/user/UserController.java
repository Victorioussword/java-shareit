package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.Create;
import ru.practicum.shareit.user.dto.Update;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@Validated(Create.class) @RequestBody UserDto userDto) {
        log.info("Gateway - UserController - createUser(). Создан {}", userDto.toString());
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable long id, @Validated(Update.class) @RequestBody UserDto userDto) {
        userDto.setId(id);
        log.info("Gateway - UserController - update(). Обновлен {}", userDto.toString());
        return userClient.update(userDto, id);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable long userId) {
        log.info("Gateway - UserController - getUser().");
        return userClient.getById(userId);
    }

    @DeleteMapping("/{id}")
    public void delById(@PathVariable Long id) {
        log.info("Gateway - UserController - delById(). Удален пользователь с id {}", id);
        userClient.delById(id);
    }


    @GetMapping
    public ResponseEntity<Object> getAll(@RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                         @RequestParam(name = "size", defaultValue = "10") @Min(1) @Max(100) Integer size) {
        log.info("Gateway - UserController - getAll(). Возвращен список всех пользователей пользователей");
        return userClient.getAll(from, size);
    }
}
