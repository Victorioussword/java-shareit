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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")

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

    @GetMapping
    public List<BookingDtoForReturn> getByBookerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        List<BookingDtoForReturn> bookingDtos = bookingService.getByBookerId(userId, State.valueOf(state));
        log.info("BookingController - getByUserId(). Возвращен список из  {} бронирований", bookingDtos.size());
        return bookingDtos;
    }

    @GetMapping("/owner")
    public List<BookingDtoForReturn> getByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        List<BookingDtoForReturn> bookingDtos = bookingService.getByOwnerId(userId, State.valueOf(state));
        log.info("BookingController - getByUserId(). Возвращен список из  {} бронирований", bookingDtos.size());
        return bookingDtos;
    }
}
