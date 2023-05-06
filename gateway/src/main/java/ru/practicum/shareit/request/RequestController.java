package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.Create;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class RequestController {

    private final RequestClient requestClient;


    //  1. POST /requests — добавить новый запрос вещи.
    @PostMapping
    public ResponseEntity<Object> createRequest(@Validated(Create.class) @RequestBody RequestInputDto requestInputDto,
                                                @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Gateway - RequestController -  createRequest(). Создан {}", requestInputDto.toString());
        return requestClient.createRequest(requestInputDto, userId);
    }


    // 2.  GET /requests — получить список своих запросов вместе с данными об ответах на них.
    @GetMapping
    public ResponseEntity<Object> getRequestsByAuthor(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Gateway - RequestController -  getRequestsByAuthor(). Возвращен список запросов");
        return requestClient.getRequestsByAuthor(userId);
    }


    // 3.  GET /requests/all?from={from}&size={size} — получить список запросов,
    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                 @RequestParam(name = "size", defaultValue = "10") @Min(1) @Max(100) Integer size) {
        return requestClient.getAllRequests(userId, from, size);
    }


    // 4. GET /requests/{requestId} — получить данные об одном конкретном запросе
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @PathVariable(name = "requestId") Long requestId) {
        log.info("Gateway - RequestController -  getRequestById().");
        return     requestClient.getRequestById(requestId, userId);
    }
}