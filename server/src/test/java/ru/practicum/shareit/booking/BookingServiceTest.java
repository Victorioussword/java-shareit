package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingDtoForReturn;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void shouldPostBooking() {
        Item item1 = new Item(1L, "item1", "description Item1", true, 2L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item1, user1, Status.WAITING);
        when(itemRepository.findById(any())).thenReturn(Optional.of(item1));
        when(userRepository.findById(any())).thenReturn(Optional.of(user1));
        when(bookingRepository.save(any())).thenReturn(booking1);
        BookingDtoForReturn bookingDtoForReturn = bookingService.postBooking(BookingMapper.toBookingDto(booking1));
        assertEquals(bookingDtoForReturn.getId(), booking1.getId());
        assertEquals(bookingDtoForReturn.getBooker().getId(), booking1.getBooker().getId());
        assertEquals(bookingDtoForReturn.getItem().getId(), booking1.getItem().getId());
        assertEquals(bookingDtoForReturn.getStart(), booking1.getStart());
        assertEquals(bookingDtoForReturn.getEnd(), booking1.getEnd());

        verify(itemRepository).findById(any());
        verify(userRepository).findById(any());
        verify(bookingRepository).save(any());

    }

    @Test
    void shouldApproving() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 2L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        when(itemRepository.getById(any())).thenReturn(item1);
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item1, user1, Status.WAITING);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));
        BookingDtoForReturn bookingDtoForReturn = bookingService.approving(1L, 2l, true);
        assertEquals(bookingDtoForReturn.getId(), booking1.getId());
        assertEquals(bookingDtoForReturn.getBooker().getId(), booking1.getBooker().getId());
        assertEquals(bookingDtoForReturn.getItem().getId(), booking1.getItem().getId());
        assertEquals(bookingDtoForReturn.getStart(), booking1.getStart());
        assertEquals(bookingDtoForReturn.getEnd(), booking1.getEnd());
        assertEquals(bookingDtoForReturn.getStatus(), Status.APPROVED);
    }

    @Test
    void shouldGetById() {
        Item item1 = new Item(1L, "item1", "description Item1", true, 2L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item1, user1, Status.WAITING);

        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking1));
        BookingDtoForReturn bookingDtoForReturnAfter = bookingService.getById(1l, 1L);

        assertEquals(bookingDtoForReturnAfter.getId(), booking1.getId());
        assertEquals(bookingDtoForReturnAfter.getBooker().getId(), booking1.getBooker().getId());
        assertEquals(bookingDtoForReturnAfter.getItem().getId(), booking1.getItem().getId());
        assertEquals(bookingDtoForReturnAfter.getStart(), booking1.getStart());
        assertEquals(bookingDtoForReturnAfter.getEnd(), booking1.getEnd());
    }

    @Test
    void shouldGetByBookerId_findAllByBookerOrderByStartDesc() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 2L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item1, user1, Status.WAITING);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerOrderByStartDesc(anyLong(), any())).thenReturn(new PageImpl<Booking>(Collections.singletonList(booking1)));
        bookingService.getByBookerId(1l, State.ALL, 1, 2);

        verify(bookingRepository).findAllByBookerOrderByStartDesc(anyLong(), any());
    }

    @Test
    void shouldGetByBookerId_findAllByBookerOrderByStartDescFuture() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 2L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item1, user1, Status.WAITING);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerOrderByStartDescFuture(anyLong(), any(), any())).thenReturn(List.of(booking1));
        bookingService.getByBookerId(1l, State.FUTURE, 0, 1);

        verify(bookingRepository).findAllByBookerOrderByStartDescFuture(anyLong(), any(), any());
    }

    @Test
    void shouldGetByBookerId_findAllByBookerOrderByStartDescStatus() {
        Item item1 = new Item(1L, "item1", "description Item1", true, 2L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item1, user1, Status.WAITING);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerOrderByStartDescStatus(anyLong(), any())).thenReturn(List.of(booking1));
        bookingService.getByBookerId(1l, State.WAITING, 0, 1);

        verify(bookingRepository).findAllByBookerOrderByStartDescStatus(anyLong(), any());
    }

    @Test
    void shouldGetByBookerId_findAllByBookerOrderByStartDescCurrent() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 2L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item1, user1, Status.WAITING);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerOrderByStartDescCurrent(anyLong(), any())).thenReturn(List.of(booking1));
        bookingService.getByBookerId(1l, State.CURRENT, 0, 1);

        verify(bookingRepository).findAllByBookerOrderByStartDescCurrent(anyLong(), any());
    }

    @Test
    void shouldGetByBookerId_findAllByBookerPast() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 2L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item1, user1, Status.WAITING);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByBookerPast(anyLong(), any())).thenReturn(List.of(booking1));
        bookingService.getByBookerId(1l, State.PAST, 0, 1);

        verify(bookingRepository).findAllByBookerPast(anyLong(), any());
    }


    @Test
    void getByOwnerId_findAllByOwnerOrderByStartDesc() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 2L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item1, user1, Status.WAITING);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByOwnerOrderByStartDesc(anyLong(), any())).thenReturn(new PageImpl<Booking>(Collections.singletonList(booking1)));
        bookingService.getByOwnerId(1l, State.ALL, 0, 1);

        verify(bookingRepository).findAllByOwnerOrderByStartDesc(anyLong(), any());
    }

    @Test
    void getByOwnerId_findAllByOwnerOrderByStartDescFuture() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 2L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item1, user1, Status.WAITING);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByOwnerOrderByStartDescFuture(anyLong(), any())).thenReturn(List.of(booking1));
        bookingService.getByOwnerId(1l, State.FUTURE, 0, 1);

        verify(bookingRepository).findAllByOwnerOrderByStartDescFuture(anyLong(), any());
    }

    @Test
    void getByOwnerId_findAllByOwnerOrderByStartDescStatus_Rejected() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 2L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item1, user1, Status.WAITING);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByOwnerOrderByStartDescStatus(anyLong(), any())).thenReturn(List.of(booking1));
        bookingService.getByOwnerId(1l, State.REJECTED, 0, 1);

        verify(bookingRepository).findAllByOwnerOrderByStartDescStatus(anyLong(), any());
    }

    @Test
    void getByOwnerId_findAllByOwnerOrderByStartDescStatus_Waiting() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 2L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item1, user1, Status.WAITING);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByOwnerOrderByStartDescStatus(anyLong(), any())).thenReturn(List.of(booking1));
        bookingService.getByOwnerId(1l, State.WAITING, 0, 1);

        verify(bookingRepository).findAllByOwnerOrderByStartDescStatus(anyLong(), any());
    }

    @Test
    void getByOwnerId_findAllByOwnerOrderByStartDescCurrent() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 2L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item1, user1, Status.WAITING);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByOwnerOrderByStartDescCurrent(anyLong(), any())).thenReturn(List.of(booking1));
        bookingService.getByOwnerId(1l, State.CURRENT, 0, 1);

        verify(bookingRepository).findAllByOwnerOrderByStartDescCurrent(anyLong(), any());
    }

    @Test
    void getByOwnerId_findAllByOwnerPast() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 2L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Booking booking1 = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item1, user1, Status.WAITING);

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(bookingRepository.findAllByOwnerPast(anyLong(), any())).thenReturn(List.of(booking1));
        bookingService.getByOwnerId(1l, State.PAST, 0, 1);

        verify(bookingRepository).findAllByOwnerPast(anyLong(), any());
    }
}
