package ru.practicum.shareit.item;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.comment.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBookingAndCommentsDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemService itemService;
    @Autowired
    MockMvc mvc;

    @Test
    void shouldGetById() throws Exception {
        Long itemId = 1L;
        Long userId = 1L;
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        ItemWithBookingAndCommentsDto withBookingAndCommentsDto1 = ItemMapper
                .toItemWithBookingAndCommentsDto(item1);

        when(itemService.getById(anyLong(), anyLong())).thenReturn(withBookingAndCommentsDto1);

        mvc.perform(get("/items/" + itemId).header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(withBookingAndCommentsDto1))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(withBookingAndCommentsDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(withBookingAndCommentsDto1.getName())))
                .andExpect(jsonPath("$.comments", is(withBookingAndCommentsDto1.getComments())))
                .andExpect(jsonPath("$.available", is(withBookingAndCommentsDto1.getAvailable())))
        ;
    }

    @Test
    void shouldPostItem() throws Exception {
        long itemId = 1;
        long userId = 1;
        Item item1 = new Item(itemId, "item1", "description Item1", true, userId, null);
        ItemDto itemDto1 = ItemMapper.toItemDto(item1);


        when(itemService.createItem(any(), anyLong())).thenReturn(itemDto1);

        mvc.perform(post("/items").header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(itemDto1))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto1)))
                .andExpect(jsonPath("$.id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto1.getAvailable())))
        ;
    }

    @Test
    void shouldUpdate() throws Exception {
        long itemId = 1;
        long userId = 1;
        Item item1 = new Item(itemId, "item1", "description Item1", true, userId, null);
        ItemDto itemDto1 = ItemMapper.toItemDto(item1);

        when(itemService.update(any(), anyLong(), anyLong())).thenReturn(itemDto1);

        mvc.perform(patch("/items/" + itemId).header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(itemDto1))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemDto1)))
                .andExpect(jsonPath("$.id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto1.getAvailable())))
        ;
    }

    @Test
    void shouldGetAllByUserId() throws Exception {
        long userId = 1;
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        ItemWithBookingAndCommentsDto itemWithBookingAndCommentsDto1 = ItemMapper.toItemWithBookingAndCommentsDto(item1);
        Item item2 = new Item(2L, "item2", "description Item2", true, 1L, null);
        ItemWithBookingAndCommentsDto itemWithBookingAndCommentsDto2 = ItemMapper.toItemWithBookingAndCommentsDto(item2);
        List<ItemWithBookingAndCommentsDto> itemWithBookingAndCommentsDtoList = new ArrayList<>();
        itemWithBookingAndCommentsDtoList.add(itemWithBookingAndCommentsDto1);
        itemWithBookingAndCommentsDtoList.add(itemWithBookingAndCommentsDto2);

        when(itemService.getAllByUserId(anyLong(), anyInt(), anyInt())).thenReturn(itemWithBookingAndCommentsDtoList);

        mvc.perform(get("/items").header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(itemWithBookingAndCommentsDtoList))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemWithBookingAndCommentsDtoList)));
    }

    @Test
    void shouldSearch() throws Exception {
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        ItemDto itemDto1 = ItemMapper.toItemDto(item1);
        Item item2 = new Item(2L, "item2", "description Item2", true, 1L, null);
        ItemDto itemDto2 = ItemMapper.toItemDto(item2);
        List<ItemDto> itemsDto = new ArrayList<>();
        itemsDto.add(itemDto1);
        itemsDto.add(itemDto2);

        when(itemService.search(anyString(), anyInt(), anyInt())).thenReturn(itemsDto);

        mvc.perform(get("/items/search").param("text", "description")
                .content(mapper.writeValueAsString(itemsDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemsDto)));
    }

    @Test
    void shouldCreateComment() throws Exception {
        LocalDateTime timeOfPost = LocalDateTime.now();
        Long itemId = 1l;
        long userId = 1;

        CommentDtoOutput commentDtoOutput = new CommentDtoOutput(1l, "Text of coment", 1l, "NameOfAuthor", timeOfPost);

        when(itemService.createComment(any(), anyLong(), anyLong())).thenReturn(commentDtoOutput);

        mvc.perform(post("/items/" + itemId + "/comment").header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(commentDtoOutput))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDtoOutput)));
    }

    @Test
    void shouldThrow500() throws Exception {
        long userId = 1;
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        ItemWithBookingAndCommentsDto itemWithBookingAndCommentsDto1 = ItemMapper.toItemWithBookingAndCommentsDto(item1);
        Item item2 = new Item(2L, "item2", "description Item2", true, 1L, null);
        ItemWithBookingAndCommentsDto itemWithBookingAndCommentsDto2 = ItemMapper.toItemWithBookingAndCommentsDto(item2);
        List<ItemWithBookingAndCommentsDto> itemWithBookingAndCommentsDtoList = new ArrayList<>();
        itemWithBookingAndCommentsDtoList.add(itemWithBookingAndCommentsDto1);
        itemWithBookingAndCommentsDtoList.add(itemWithBookingAndCommentsDto2);

        when(itemService.getAllByUserId(anyLong(), anyInt(), anyInt())).thenReturn(itemWithBookingAndCommentsDtoList);

        mvc.perform(get("/items").header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(itemWithBookingAndCommentsDtoList))
                .param("from", String.valueOf(-1))
                .param("size", String.valueOf(-2))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                ;
    }
}