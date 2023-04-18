package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.Request;

import java.util.ArrayList;

public class RequestMapper {


    public static RequestOutputDto toRequestOutputDto(Request request) {

        return new RequestOutputDto(request.getId(),
                request.getDescription(),
                request.getRequester(),
                request.getCreated(),
                new ArrayList<>());  // список Item
    }


    public static Request toRequest(RequestInputDto requestInputDto) {
        return new Request(requestInputDto.getId(),
                requestInputDto.getDescription(),
                requestInputDto.getRequester(),
                requestInputDto.getCreated());
    }
}