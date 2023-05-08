package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.RequestInputDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.dto.RequestOutputDto;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestMapperTest {

    @Test
    void toRequestOutputDtoTest() {
        Request request = new Request(1L,
                " descriptionOfRequest1",
                1L,
                LocalDateTime.now());
        RequestOutputDto requestOutputDto = RequestMapper.toRequestOutputDto(request);

        assertEquals(request.getId(), requestOutputDto.getId());
        assertEquals(request.getDescription(), requestOutputDto.getDescription());
        assertEquals(request.getDescription(), requestOutputDto.getDescription());
        assertEquals(request.getCreated(), requestOutputDto.getCreated());
    }

    @Test
    void toRequestTest() {
        RequestInputDto requestInputDto = new RequestInputDto(
                " descriptionOfRequest1");
        Request request = RequestMapper.toRequest(requestInputDto, 1L);
                assertEquals(request.getDescription(), requestInputDto.getDescription());
    }
}
