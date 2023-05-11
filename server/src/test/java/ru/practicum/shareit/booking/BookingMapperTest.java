package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForReturn;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingMapperTest {


    @Test
    void toBookingTest() {
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        BookingDto bookingDto1 = new BookingDto(1L, LocalDateTime.now(), LocalDateTime.now(), 1L, 1L);

        Booking booking1 = BookingMapper.toBooking(bookingDto1, user1, item1);

        assertEquals(booking1.getId(), bookingDto1.getId());
        assertEquals(item1.getId(), bookingDto1.getItemId());
        assertEquals(user1.getId(), bookingDto1.getBooker());
        assertNotNull(booking1.getStart());
        assertNotNull(booking1.getEnd());
        assertEquals(booking1.getStatus().toString(), Status.WAITING.toString());
    }

    @Test
    void toBookingDtoTest() {
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), item1, user1, Status.WAITING);

        BookingDto bookingDto1 = BookingMapper.toBookingDto(booking1);

        assertEquals(bookingDto1.getId(), booking1.getId());
        assertEquals(bookingDto1.getStart(), booking1.getStart());
        assertEquals(bookingDto1.getEnd(), booking1.getEnd());
        assertEquals(bookingDto1.getItemId(), booking1.getItem().getId());
        assertEquals(bookingDto1.getBooker(), booking1.getBooker().getId());
    }

    @Test
    void toBookingDtoForReturnTest() {
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), item1, user1, Status.WAITING);

        BookingDtoForReturn bookingDtoForReturn1 = BookingMapper.toBookingDtoForReturn(booking1);

        assertEquals(bookingDtoForReturn1.getId(), booking1.getId());
        assertEquals(bookingDtoForReturn1.getStart(), booking1.getStart());
        assertEquals(bookingDtoForReturn1.getEnd(), booking1.getEnd());
        assertEquals(bookingDtoForReturn1.getItem().getId(), booking1.getItem().getId());
        assertEquals(bookingDtoForReturn1.getBooker().getId(), booking1.getBooker().getId());
        assertEquals(bookingDtoForReturn1.getStatus().toString(), booking1.getStatus().toString());
    }

    @Test
    void toBookingShortDtoTest() {
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(), item1, user1, Status.WAITING);

        BookingShortDto bookingShortDto1 = BookingMapper.toBookingShortDto(booking1);

        assertEquals(bookingShortDto1.getId(), booking1.getId());
        assertEquals(bookingShortDto1.getBookerId(), booking1.getBooker().getId());
    }
}
