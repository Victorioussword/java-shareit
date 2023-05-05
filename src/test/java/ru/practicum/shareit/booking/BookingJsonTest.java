package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoForReturn;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingJsonTest {

    @Autowired
    private JacksonTester<BookingDtoForReturn> bookingDtoForReturnJacksonTester;

    private BookingDtoForReturn bookingDtoForReturn;

    @BeforeEach
    void beforeEach() {
        LocalDateTime now = LocalDateTime.now();

        User user1 = new User(1L, "UserName1", "user1@mail.com");
        Item item1 = new Item(1L, "Item1", "description of Item1", true, 1L, null);

        Booking booking1 = new Booking(1L, now.plusDays(1), now.plusDays(2), item1, user1, Status.WAITING);

        bookingDtoForReturn = BookingMapper.toBookingDtoForReturn(booking1);
    }

    @Test
    void testOfSerializing() throws Exception {
        JsonContent<BookingDtoForReturn> result = bookingDtoForReturnJacksonTester.write(bookingDtoForReturn);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.item");
        assertThat(result).hasJsonPath("$.booker");
        assertThat(result).hasJsonPath("$.status");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(Math.toIntExact(bookingDtoForReturn.getId()));
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(Math.toIntExact(bookingDtoForReturn.getItem().getId()));
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(Math.toIntExact(bookingDtoForReturn.getBooker().getId()));
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(bookingDtoForReturn.getStatus().toString());
    }
}
