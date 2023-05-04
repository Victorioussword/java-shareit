package ru.practicum.shareit.request;


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
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemShortForRequest;
import ru.practicum.shareit.request.dto.RequestInputDto;
import ru.practicum.shareit.request.dto.RequestOutputDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@FieldDefaults(level = AccessLevel.PRIVATE)

public class RequestControllerTest {


    @Autowired
    ObjectMapper mapper;
    @MockBean
    RequestService requestService;
    @Autowired
    MockMvc mvc;

    @Test
    void createRequestTest() throws Exception {
        LocalDateTime time = LocalDateTime.now();
        Request request = new Request(1l,
                " descriptionOfRequest1",
                1l,
                LocalDateTime.now());
        Long userId = 1L;
        RequestInputDto requestInputDto = new RequestInputDto(
                " descriptionOfRequest1");
        List<ItemShortForRequest> itemShortForRequests = new ArrayList<>();
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, request);
        ItemShortForRequest itemShortForRequest1 = ItemMapper.toItemShortForRequest(item1);

        Item item2 = new Item(2L, "item2", "description Item2", true, 1L, request);
        ItemShortForRequest itemShortForRequest2 = ItemMapper.toItemShortForRequest(item2);
        itemShortForRequests.add(itemShortForRequest1);
        itemShortForRequests.add(itemShortForRequest2);

        RequestOutputDto requestOutputDto = new RequestOutputDto(1L,
                requestInputDto.getDescription(),
                userId,
                 time,
                itemShortForRequests);

        when(requestService.createRequest(any(), anyLong())).thenReturn(requestOutputDto);

        mvc.perform(post("/requests").header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(requestOutputDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestOutputDto)))
        ;
    }

    @Test
    void getRequestsByAuthorTest() throws Exception {
        Long userId = 1l;
        RequestOutputDto requestOutputDto1 = new RequestOutputDto(1l,
                " descriptionOfRequest1",
                1l,
                LocalDateTime.now(),
                new ArrayList<>());
        RequestOutputDto requestOutputDto2 = new RequestOutputDto(2l,
                " descriptionOfRequest1",
                1l,
                LocalDateTime.now(),
                new ArrayList<>());

        List<RequestOutputDto> requestOutputDtos = new ArrayList<>();
        requestOutputDtos.add(requestOutputDto1);
        requestOutputDtos.add(requestOutputDto2);

        when(requestService.getRequestsByAuthor(anyLong())).thenReturn(requestOutputDtos);

        mvc.perform(get("/requests").header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(requestOutputDtos))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestOutputDtos)))
        ;
    }

    @Test
    void getAllRequestsTest() throws Exception {
        Long userId = 1l;
        RequestOutputDto requestOutputDto1 = new RequestOutputDto(1l,
                " descriptionOfRequest1",
                1l,
                LocalDateTime.now(),
                new ArrayList<>());
        RequestOutputDto requestOutputDto2 = new RequestOutputDto(2l,
                " descriptionOfRequest1",
                1l,
                LocalDateTime.now(),
                new ArrayList<>());

        List<RequestOutputDto> requestOutputDtos = new ArrayList<>();
        requestOutputDtos.add(requestOutputDto1);
        requestOutputDtos.add(requestOutputDto2);

        when(requestService.getAllRequests(anyLong(), anyInt(), anyInt())).thenReturn(requestOutputDtos);

        mvc.perform(get("/requests/all").header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(requestOutputDtos))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestOutputDtos)))
        ;
    }

    @Test
    void getRequestByIdTest() throws Exception {
        Request request = new Request(1l,
                " descriptionOfRequest1",
                1l,
                LocalDateTime.now());
        Long userId = 1L;
        Long requestId = 1L;
        LocalDateTime time = LocalDateTime.now();
        RequestInputDto requestInputDto = new RequestInputDto(
                " descriptionOfRequest1"
                );
        List<ItemShortForRequest> itemShortForRequests = new ArrayList<>();
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, request);
        ItemShortForRequest itemShortForRequest1 = ItemMapper.toItemShortForRequest(item1);

        Item item2 = new Item(2L, "item2", "description Item2", true, 1L, request);
        ItemShortForRequest itemShortForRequest2 = ItemMapper.toItemShortForRequest(item2);
        itemShortForRequests.add(itemShortForRequest1);
        itemShortForRequests.add(itemShortForRequest2);

        RequestOutputDto requestOutputDto = new RequestOutputDto(1L,
                requestInputDto.getDescription(),
                userId,
                time,
                itemShortForRequests);

        when(requestService.getRequestById(anyLong(), anyLong())).thenReturn(requestOutputDto);

        mvc.perform(get("/requests/" + requestId)
                .header("X-Sharer-User-Id", userId)

                .content(mapper.writeValueAsString(requestOutputDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(requestOutputDto)))
        ;


    }
}