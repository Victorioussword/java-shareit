package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.exception.UnknownStatusException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
/**
 * GATEWAY
 */
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Validated BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }


    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new UnknownStatusException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
                                               @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                               @RequestParam(name = "size", defaultValue = "10") @Min(1) @Max(100) Integer size
    ) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new UnknownStatusException("Unknown state: " + stateParam));
        log.info("BookingController - getByUserId(). Возвращен список из бронирований");
        return bookingClient.getByOwnerId(userId, state, from, size);
    }


    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approving(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @PathVariable long bookingId,
                                            @RequestParam boolean approved) {
        log.info("BookingController - approving(). Для Booking {}, установлен статус {} ", bookingId, approved);
        return bookingClient.approving(bookingId, userId, approved);
    }


    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }


}
