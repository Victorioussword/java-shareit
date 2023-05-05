package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(controllers = UserController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    UserService userService;
    @Autowired
    MockMvc mvc;


    @Test
    void shouldReturnUserList() throws Exception {
        User user1 = new User(1L, "userName1", "user1@mail.ru");
        User user2 = new User(2L, "userName2", "user2@mail.ru");
        UserDto userDto1 = UserMapper.toUserDto(user1);
        UserDto userDto2 = UserMapper.toUserDto(user2);
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(userDto1);
        userDtos.add(userDto2);

        when(userService.getAll(anyInt(), anyInt())).thenReturn(userDtos);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userDtos)));
    }

    @Test
    void shouldReturnUser() throws Exception {
        Long id = 1L;
        User user1 = new User(id, "userName1", "user@user.com");

        UserDto userDto1 = UserMapper.toUserDto(user1);

        when(userService.getById(any())).thenReturn(userDto1);

        mvc.perform(get("/users/" + id)
                .content(mapper.writeValueAsString(userDto1))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto1.getName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
    }


    @Test
    void shouldCreateUser() throws Exception {
        Long id = 1L;
        User user1 = new User(id, "userName1", "user@user.com");

        UserDto userDto1 = UserMapper.toUserDto(user1);

        when(userService.createUser(any())).thenReturn(userDto1);

        mvc.perform(post("/users")
                .content(mapper.writeValueAsString(userDto1))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto1.getName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        Long userId = 1L;
        User user1 = new User(1L, "userName1", "user@user.com");
        UserDto userDto1 = UserMapper.toUserDto(user1);

        when(userService.update(any(), anyLong())).thenReturn(userDto1);

        mvc.perform(patch("/users/" + userId)
                .content(mapper.writeValueAsString(userDto1))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto1.getName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        Long userId = 1L;
        when(userService.update(any(), anyLong())).thenReturn(null);
        User user1 = new User(1L, "userName1", "user@user.com");
        UserDto userDto1 = UserMapper.toUserDto(user1);

        mvc.perform(patch("/users/" + userId)
                .content(mapper.writeValueAsString(userDto1))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void shouldReturn400() throws Exception {
        Long userId = 1l;
        User user1 = new User(1L, "userName1", "user1@mail.ru");
        User user2 = new User(2L, "userName2", "user2@mail.ru");
        UserDto userDto1 = UserMapper.toUserDto(user1);
        UserDto userDto2 = UserMapper.toUserDto(user2);
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(userDto1);
        userDtos.add(userDto2);

        when(userService.getAll(anyInt(), anyInt())).thenReturn(userDtos);

        mvc.perform(get("/users").header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(userDtos))
                .param("from", String.valueOf(-1))
                .param("size", String.valueOf(-2))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

}

