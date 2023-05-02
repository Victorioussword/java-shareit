package ru.practicum.shareit.booking.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForReturn;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.dto.Create;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoForReturn postBooking(@RequestHeader("X-Sharer-User-Id") long bookerId,
                                           @Validated(Create.class) @RequestBody BookingDto bookingDto) {
        bookingDto.setBooker(bookerId);
        log.info("BookingController - postBooking().  ДОбавлен  {}", bookingDto.toString());
        return bookingService.postBooking(bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoForReturn approving(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable long bookingId,
                                         @RequestParam boolean approved) {
        log.info("BookingController - approving(). Для Booking {}, установлен статус {} ", bookingId, approved);
        return bookingService.approving(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoForReturn getById(@PathVariable long bookingId, @RequestHeader("X-Sharer-User-Id") long userId) {
        BookingDtoForReturn bookingDtoForReturn = bookingService.getById(bookingId, userId);
        log.info("BookingController - getById(). Возвращен {}", bookingDtoForReturn);
        return bookingDtoForReturn;
    }

    @GetMapping("/owner")
    public List<BookingDtoForReturn> getByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(defaultValue = "ALL") String state,
                                                  @RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) @Max(100) Integer size
                                                  ) {
        List<BookingDtoForReturn> bookingDtos = bookingService.getByOwnerId(userId, State.valueOf(state), from, size);
        log.info("BookingController - getByUserId(). Возвращен список из  {} бронирований", bookingDtos.size());
        return bookingDtos;
    }

    @GetMapping
    public List<BookingDtoForReturn> getByBookerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(name = "from", required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(name = "size", required = false, defaultValue = "10") @Positive @Max(100) Integer size
                                                   ) {
        List<BookingDtoForReturn> bookingDtos = bookingService.getByBookerId(userId, State.valueOf(state), from, size);
        log.info("BookingController - getByUserId(). Возвращен список из  {} бронирований", bookingDtos.size());
        return bookingDtos;
    }
}