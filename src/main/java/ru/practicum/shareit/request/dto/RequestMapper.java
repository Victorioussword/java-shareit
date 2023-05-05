package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;

@UtilityClass
public class RequestMapper {

    public static RequestOutputDto toRequestOutputDto(Request request) {
        return new RequestOutputDto(request.getId(),
                request.getDescription(),
                request.getRequester(),
                request.getCreated(),
                new ArrayList<>());  // список Item
    }

    public static Request toRequest(RequestInputDto requestInputDto, Long userId) {
        return new Request(0L,
                requestInputDto.getDescription(),
                userId,
                LocalDateTime.now());
    }
}