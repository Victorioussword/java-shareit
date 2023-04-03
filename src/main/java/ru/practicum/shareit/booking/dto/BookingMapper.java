package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemShort;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserShort;

@UtilityClass
public class BookingMapper {

    public static Booking toBooking(BookingDto bookingDto, User user, Item item) {
        return new Booking(bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                item,
                user,
                null);
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId()
        );
    }

    public static BookingDtoForReturn toBookingDtoForReturn(Booking booking) {
        return new BookingDtoForReturn(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new ItemShort(booking.getItem().getId(), booking.getItem().getName()),
                new UserShort(booking.getBooker().getId()),
                Status.valueOf(booking.getStatus()));
    }

    public static BookingShortDto toBookingShortDto(Booking booking) {
        return new BookingShortDto(booking.getId(),
                booking.getBooker().getId());
    }
}