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
        Request request = new Request(1l,
                " descriptionOfRequest1",
                1l,
                LocalDateTime.now());
        RequestOutputDto requestOutputDto = RequestMapper.toRequestOutputDto(request);

        assertEquals(request.getId(), requestOutputDto.getId());
        assertEquals(request.getDescription(), requestOutputDto.getDescription());
        assertEquals(request.getDescription(), requestOutputDto.getDescription());
        assertEquals(request.getCreated(), requestOutputDto.getCreated());
    }

    @Test
    void toRequestTest() {
        RequestInputDto requestInputDto = new RequestInputDto(1l,
                " descriptionOfRequest1",
                1l,
                LocalDateTime.now());
        Request request = RequestMapper.toRequest(requestInputDto);

        assertEquals(request.getId(), requestInputDto.getId());
        assertEquals(request.getDescription(), requestInputDto.getDescription());
        assertEquals(request.getDescription(), requestInputDto.getDescription());
        assertEquals(request.getCreated(), requestInputDto.getCreated());
    }
}
