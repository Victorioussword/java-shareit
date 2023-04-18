package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestInputDto;
import ru.practicum.shareit.request.dto.RequestOutputDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.Create;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestService requestService;


    //  1. POST /requests — добавить новый запрос вещи.
    @PostMapping
    public RequestOutputDto createRequest(@Validated(Create.class) @RequestBody RequestInputDto requestInputDto,
                                          @RequestHeader("X-Sharer-User-Id") long userId) {  // todo добавить автора запроса
        log.info(" RequestController -  createRequest(). Создан {}", requestInputDto.toString());
        return requestService.createRequest(requestInputDto, userId);
    }

    // 2.  GET /requests — получить список своих запросов вместе с данными об ответах на них.
    @GetMapping
    public List<RequestOutputDto> getRequestsByAuthor(@RequestHeader("X-Sharer-User-Id") long userId) {

        List<RequestOutputDto> requestOutputDtos = requestService.getRequestsByAuthor(userId);
        log.info(" RequestController -  getRequestsByAuthor(). Возвращен список из {} запросов", requestOutputDtos.size());
        return requestOutputDtos;
    }

    // 3.  GET /requests/all?from={from}&size={size} — получить список запросов,
    @GetMapping("/all")
    public List<RequestOutputDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                                 @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) @Max(100) Integer size) {
        return requestService.getAllRequests(userId, from, size);
    }

    // 4. GET /requests/{requestId} — получить данные об одном конкретном запросе
    @GetMapping("/{requestId}")
    public RequestOutputDto getRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @PathVariable(required = true, name = "requestId") Long requestId) {
        RequestOutputDto requestOutputDto = requestService.getRequestById(requestId, userId);
        log.info(" RequestController -  getRequestById(). Возвращен {}", requestOutputDto.toString());
        return requestOutputDto;
    }

}