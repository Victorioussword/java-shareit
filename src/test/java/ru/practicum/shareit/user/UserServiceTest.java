package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldgetById() {
        long id = 1;
        User user1 = new User(1L, "userName", "user@mail.ru");
        UserDto userDto1 = UserMapper.toUserDto(user1);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        assertThat(userService.getById(anyLong()).getId()).isEqualTo(userDto1.getId());
        assertThat(userService.getById(anyLong()).getName()).isEqualTo(userDto1.getName());
        assertThat(userService.getById(anyLong()).getEmail()).isEqualTo(userDto1.getEmail());
    }


    @Test
    void shouldThrowNotFoundException() { // getById()
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getById(1l)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldCreateUser() {
        User user1 = new User(1l, "userName", "user@mail.ru");
        when(userRepository.save(any())).thenReturn(user1);
        UserDto userAfterSave = userService.createUser(UserMapper.toUserDto(user1));
        assertThat(userAfterSave.getId()).isEqualTo(user1.getId());
        assertThat(userAfterSave.getName()).isEqualTo(user1.getName());
        assertThat(userAfterSave.getEmail()).isEqualTo(user1.getEmail());
    }

    @Test
    void update_shouldThrowNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> userService.getById(anyLong())).isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldUpdate() {
        User user1 = new User(1L, "userName", "user@mail.ru");
        Optional<User> us = Optional.of(user1);

        UserDto userDto1 = UserMapper.toUserDto(user1);

        when(userRepository.findById(1l)).thenReturn(us);

        UserDto userDtoAfter = userService.update(userDto1, 1l);

        assertThat(userDtoAfter.getId()).isEqualTo(user1.getId());
        assertThat(userDtoAfter.getName()).isEqualTo(user1.getName());
        assertThat(userDtoAfter.getEmail()).isEqualTo(user1.getEmail());
    }

    @Test
    void shouldGetAll() {
        User user1 = new User(1L, "userName1", "user1@mail.ru");
        UserDto userDto1 = UserMapper.toUserDto(user1);
        User user2 = new User(2L, "userName2", "user2@mail.ru");
        UserDto userDto2 = UserMapper.toUserDto(user1);

        List<UserDto> dtos = new ArrayList<>();
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        dtos.add(userDto1);
        dtos.add(userDto2);
        when(userRepository.findAll()).thenReturn(users);
        List<UserDto> usersAfter = userService.getAll();

        assertThat(usersAfter.size()).isEqualTo(users.size());

        assertThat(usersAfter.get(0).getId()).isEqualTo(users.get(0).getId());
        assertThat(usersAfter.get(0).getName()).isEqualTo(users.get(0).getName());
        assertThat(usersAfter.get(0).getEmail()).isEqualTo(users.get(0).getEmail());

        assertThat(usersAfter.get(1).getId()).isEqualTo(users.get(1).getId());
        assertThat(usersAfter.get(1).getName()).isEqualTo(users.get(1).getName());
        assertThat(usersAfter.get(1).getEmail()).isEqualTo(users.get(1).getEmail());
    }
}