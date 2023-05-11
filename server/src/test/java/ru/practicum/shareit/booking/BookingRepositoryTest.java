package ru.practicum.shareit.booking;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private BookingRepository bookingRepository;
    PageRequest pageRequest;
    User user1 = new User();
    User user2 = new User();
    Request request1;
    Request request2;
    Item item1 = new Item();
    Item item2 = new Item();
    Booking booking1 = new Booking();    // Будущее - ожидает
    Booking booking2 = new Booking();    // прошлое - подтверждено
    Booking booking3 = new Booking();     // прошлое - отклоенено
    Booking booking4 = new Booking();    // настоящее время - текущее

    @BeforeEach
    public void before() {
        pageRequest = PageRequest.of(0, 4);
        user1 = new User(1L, "userName1", "user1@user.com");
        user2 = new User(2L, "userName2", "user2@user.com");
        request1 = new Request(1L, " descriptionOfRequest1", 2L, LocalDateTime.now().minusDays(2));
        request2 = new Request(2L, " descriptionOfRequest2", 1L, LocalDateTime.now().minusDays(1));
        item1 = new Item(1L, "item1", "description Item1", true, 1L, request1);
        item2 = new Item(2L, "item2", "description Item2", true, 1L, request2);
        booking1 = new Booking(1L, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), item1, user1, Status.WAITING);
        booking2 = new Booking(2L, LocalDateTime.now().minusDays(4), LocalDateTime.now().minusDays(3), item1, user1, Status.APPROVED);
        booking3 = new Booking(3L, LocalDateTime.now().minusDays(6), LocalDateTime.now().minusDays(5), item1, user1, Status.REJECTED);
        booking4 = new Booking(4L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), item1, user1, Status.APPROVED);
        userRepository.save(user1);
        userRepository.save(user2);
        requestRepository.save(request1);
        requestRepository.save(request2);
        itemRepository.save(item1);
        itemRepository.save(item2);
        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
    }

    @Test
    void contextLoads() {
        assertThat(em).isNotNull();
    }

    @DirtiesContext
    @Test
    void shouldFindAllByBookerOrderByStartDesc() {

        Page<Booking> bookings = bookingRepository.findAllByBookerOrderByStartDesc(1, pageRequest);
        List<Booking> bookingList = bookings.toList();
        assertEquals(4, bookingList.size());
        assertEquals(booking1.getStart(), bookingList.get(0).getStart());
        assertEquals(booking3.getStart(), bookingList.get(3).getStart());
    }

    @DirtiesContext
    @Test
    void shouldFindAllByBookerOrderByStartDescStatus() {

        List<Booking> bookings = bookingRepository.findAllByBookerOrderByStartDescStatus(1, Status.APPROVED);
        assertEquals(2, bookings.size());
        assertEquals(booking4.getStart(), bookings.get(0).getStart());
        assertEquals(booking2.getStart(), bookings.get(1).getStart());
    }

    @DirtiesContext
    @Test
    void shouldFindAllByBookerPast() {

        List<Booking> bookings = bookingRepository.findAllByBookerPast(1, LocalDateTime.now());
        assertEquals(2, bookings.size());
        assertEquals(booking2.getStart(), bookings.get(0).getStart());
        assertEquals(booking3.getStart(), bookings.get(1).getStart());
    }

    @DirtiesContext
    @Test
    void shouldFindAllByBookerOrderByStartDescCurrent() {

        List<Booking> bookings = bookingRepository.findAllByBookerOrderByStartDescCurrent(1, LocalDateTime.now());
        assertEquals(1, bookings.size());
        assertEquals(booking4.getStart(), bookings.get(0).getStart());
    }

    @DirtiesContext
    @Test
    void shouldFindAllByBookerOrderByStartDescFuture() {

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings = bookingRepository.findAllByBookerOrderByStartDescFuture(1, LocalDateTime.now(), sort);
        assertEquals(1, bookings.size());
        assertEquals(booking1.getStart(), bookings.get(0).getStart());
    }

    @DirtiesContext
    @Test
    void shouldFindAllByOwnerOrderByStartDesc() {

        Page<Booking> bookings = bookingRepository.findAllByOwnerOrderByStartDesc(1, pageRequest);
        List<Booking> bookingList = bookings.toList();
        assertEquals(4, bookingList.size());
        assertEquals(booking1.getStart(), bookingList.get(0).getStart());
        assertEquals(booking3.getStart(), bookingList.get(3).getStart());
    }

    @DirtiesContext
    @Test
    void shouldFindAllByOwnerOrderByStartDescFuture() {

        List<Booking> bookingList = bookingRepository.findAllByOwnerOrderByStartDescFuture(1, LocalDateTime.now());
        assertEquals(1, bookingList.size());
        assertEquals(booking1.getStart(), bookingList.get(0).getStart());
        assertEquals(booking1.getId(), bookingList.get(0).getId());
    }

    @DirtiesContext
    @Test
    void shouldFindAllByOwnerOrderByStartDescStatus() {

        List<Booking> bookingList = bookingRepository.findAllByOwnerOrderByStartDescStatus(1,Status.REJECTED);
        assertEquals(1, bookingList.size());
        assertEquals(booking3.getStart(), bookingList.get(0).getStart());
        assertEquals(booking3.getId(), bookingList.get(0).getId());
    }

    @DirtiesContext
    @Test
    void shouldFindAllByOwnerOrderByStartDescCurrent() {

        List<Booking> bookingList = bookingRepository.findAllByOwnerOrderByStartDescCurrent(1, LocalDateTime.now());
        assertEquals(1, bookingList.size());
        assertEquals(booking4.getStart(), bookingList.get(0).getStart());
        assertEquals(booking4.getId(), bookingList.get(0).getId());
    }

    @DirtiesContext
    @Test
    void shouldFindAllByOwnerPast() {

        List<Booking> bookingList = bookingRepository.findAllByOwnerPast(1, LocalDateTime.now());
        assertEquals(2, bookingList.size());
        assertEquals(booking2.getStart(), bookingList.get(0).getStart());
        assertEquals(booking2.getId(), bookingList.get(0).getId());
        assertEquals(booking3.getStart(), bookingList.get(1).getStart());
        assertEquals(booking3.getId(), bookingList.get(1).getId());
    }
}