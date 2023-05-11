package ru.practicum.shareit.item;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.comment.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemWithBookingAndCommentsDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemJsonTest {


    @Autowired
    private JacksonTester<ItemWithBookingAndCommentsDto> tester;

    private ItemWithBookingAndCommentsDto itemWithBookingAndCommentsDto;

    @BeforeEach
    void beforeEach() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        ArrayList<CommentDtoOutput> arrayList = new ArrayList();
        itemWithBookingAndCommentsDto = ItemMapper.toItemWithBookingAndCommentsDto(item1);
        User user1 = new User(1L, "userName1", "user1@mail.ru");
        User user2 = new User(2L, "userName2", "user2@mail.ru");
        Booking bookingLast = new Booking(2L, LocalDateTime.now().minusDays(6), LocalDateTime.now().minusDays(5), item1, user1, Status.WAITING);
        Booking bookingNext = new Booking(2L, LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(5), item1, user1, Status.WAITING);
        itemWithBookingAndCommentsDto.setLastBooking(BookingMapper.toBookingShortDto(bookingLast));
        itemWithBookingAndCommentsDto.setNextBooking(BookingMapper.toBookingShortDto(bookingNext));
        CommentDtoOutput comm1 = new CommentDtoOutput(1L, "CommentText", user2.getId(), user2.getName(), LocalDateTime.now());
        List<CommentDtoOutput> comments = List.of(comm1);
        itemWithBookingAndCommentsDto.setComments(comments);
    }

    @Test
    void testOfSerializing() throws Exception {
        JsonContent<ItemWithBookingAndCommentsDto> result = tester.write(itemWithBookingAndCommentsDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.lastBooking");
        assertThat(result).hasJsonPath("$.nextBooking");
        assertThat(result).hasJsonPath("$.comments");
    }
}
