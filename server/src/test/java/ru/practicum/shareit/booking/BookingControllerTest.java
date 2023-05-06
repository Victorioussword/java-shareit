package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoForReturn;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemShort;
import ru.practicum.shareit.user.model.UserShort;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;
    @MockBean
    BookingService bookingService;
    @Autowired
    MockMvc mvc;

    @Test
    void shouldPostBooking() throws Exception {
        Long userId = 1L;
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        UserShort userShort1 = new UserShort(1l);

        BookingDtoForReturn bookingDtoForReturn = new BookingDtoForReturn(1l,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new ItemShort(item1.getId(), item1.getName()),
                userShort1,
                Status.WAITING);

        when(bookingService.postBooking(any())).thenReturn(bookingDtoForReturn);
        mvc.perform(post("/bookings")
                .header("X-Sharer-User-Id", userId)

                .content(mapper.writeValueAsString(bookingDtoForReturn))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoForReturn)));
    }

    @Test
    void shouldApprove() throws Exception {
        Long userId = 1L;
        Long bookingId = 1L;
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        UserShort userShort1 = new UserShort(1l);

        BookingDtoForReturn bookingDtoForReturn = new BookingDtoForReturn(1l,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new ItemShort(item1.getId(), item1.getName()),
                userShort1,
                Status.WAITING);

        when(bookingService.approving(anyLong(), anyLong(), any())).thenReturn(bookingDtoForReturn);
        mvc.perform(patch("/bookings/" + bookingId)
                .param("approved", "true")
                .header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(bookingDtoForReturn))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoForReturn)));
    }

    @Test
    void shouldGetByOwnerId() throws Exception {
        Long userId = 1L;
        UserShort userShort1 = new UserShort(1l);

        BookingDtoForReturn bookingDtoForReturn1 = new BookingDtoForReturn(1l,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new ItemShort(1l, "ItemName1"),
                userShort1,
                Status.WAITING);

        BookingDtoForReturn bookingDtoForReturn2 = new BookingDtoForReturn(2l,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(3),
                new ItemShort(1l, "ItemName2"),
                userShort1,
                Status.APPROVED);

        List<BookingDtoForReturn> listBookings = List.of(bookingDtoForReturn1, bookingDtoForReturn2);

        when(bookingService.getByOwnerId(anyLong(), any(), anyInt(), anyInt())).thenReturn(listBookings);

        mvc.perform(get("/bookings/owner")
                .header("X-Sharer-User-Id", userId)
                .param("from", String.valueOf(1))
                .param("size", String.valueOf(2))
                .content(mapper.writeValueAsString(listBookings))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(listBookings)));
    }

    @Test
    void shouldGetById() throws Exception {
        Long userId = 1L;
        Long bookingId = 1l;
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        UserShort userShort1 = new UserShort(1l);

        BookingDtoForReturn bookingDtoForReturn = new BookingDtoForReturn(1l,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new ItemShort(item1.getId(), item1.getName()),
                userShort1,
                Status.WAITING);

        when(bookingService.getById(anyLong(), anyLong())).thenReturn(bookingDtoForReturn);
        mvc.perform(get("/bookings/" + bookingId)
                .header("X-Sharer-User-Id", userId)
                .content(mapper.writeValueAsString(bookingDtoForReturn))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDtoForReturn)));
    }

    @Test
    void shouldGetByBookerId() throws Exception {

        Long userId = 1L;
        UserShort userShort1 = new UserShort(1l);

        BookingDtoForReturn bookingDtoForReturn1 = new BookingDtoForReturn(1l,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new ItemShort(1l, "ItemName1"),
                userShort1,
                Status.WAITING);

        BookingDtoForReturn bookingDtoForReturn2 = new BookingDtoForReturn(2l,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(3),
                new ItemShort(1l, "ItemName2"),
                userShort1,
                Status.APPROVED);

        List<BookingDtoForReturn> listBookings = List.of(bookingDtoForReturn1, bookingDtoForReturn2);

        when(bookingService.getByBookerId(anyLong(), any(), anyInt(), anyInt())).thenReturn(listBookings);

        mvc.perform(get("/bookings")
                .header("X-Sharer-User-Id", userId)
                .param("from", String.valueOf(1))
                .param("size", String.valueOf(2))
                .content(mapper.writeValueAsString(listBookings))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(listBookings)));
    }

    @Test
            void shouldReturnStatus400() throws Exception {
        Long userId = 1L;
        UserShort userShort1 = new UserShort(1l);

        BookingDtoForReturn bookingDtoForReturn1 = new BookingDtoForReturn(1l,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                new ItemShort(1l, "ItemName1"),
                userShort1,
                Status.WAITING);

        BookingDtoForReturn bookingDtoForReturn2 = new BookingDtoForReturn(2l,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(3),
                new ItemShort(1l, "ItemName2"),
                userShort1,
                Status.APPROVED);

        List<BookingDtoForReturn> listBookings = List.of(bookingDtoForReturn1, bookingDtoForReturn2);

        when(bookingService.getByBookerId(anyLong(), any(), anyInt(), anyInt())).thenReturn(listBookings);

        mvc.perform(get("/bookings")
                .header("X-Sharer-User-Id", userId)
                .param("from", String.valueOf(-1))
                .param("size", String.valueOf(-2))
                .content(mapper.writeValueAsString(listBookings))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
        ;
    }
}


