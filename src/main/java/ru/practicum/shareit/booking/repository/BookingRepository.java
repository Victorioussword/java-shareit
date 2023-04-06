package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {


    @Query("select booking from Booking booking " +
            "where booking.booker.id = ?1 " +
            "order by booking.start desc")
    List<Booking> findAllByBookerOrderByStartDesc(long bookerId);


    @Query("select booking from Booking booking " +
            "where booking.status = ?2 " +
            "and booking.booker.id = ?1 " +
            "order by booking.start desc")
    List<Booking> findAllByBookerOrderByStartDescStatus(long userId, Status status);


    @Query("select booking from Booking booking " +
            "where booking.start < ?2 " +
            "and booking.end < ?2 " +
            "and booking.booker.id = ?1 " +
            "order by booking.start desc")
    List<Booking> findAllByBookerPast(long userId, LocalDateTime now);


    @Query("select booking from Booking booking " +
            "where booking.start < ?2 " +
            "and booking.end > ?2 " +
            "and booking.booker.id = ?1 " +
            "order by booking.start desc")
    List<Booking> findAllByBookerOrderByStartDescCurrent(long userId, LocalDateTime now);


    @Query("select booking from Booking booking " +
            "where booking.start >= ?2 " +
            "and booking.booker.id = ?1 ")
    List<Booking> findAllByBookerOrderByStartDescFuture(long userId, LocalDateTime now, Sort sort);


    @Query("select booking from Booking booking " +
            "where booking.item.owner = ?1 " +
            "order by booking.start desc")
    List<Booking> findAllByOwnerOrderByStartDesc(long ownerId);


    @Query("select booking from Booking booking " +
            "where booking.start > ?2 " +
            "and booking.end > ?2 " +
            "and booking.item.owner = ?1 " +
            "order by booking.start desc")
    List<Booking> findAllByOwnerOrderByStartDescFuture(long ownerId, LocalDateTime now);


    @Query("select booking from Booking booking " +
            "where booking.status = ?2 " +
            "and booking.item.owner = ?1 " +
            "order by booking.status desc")
    List<Booking> findAllByOwnerOrderByStartDescStatus(long ownerId, Status status);


    @Query("select booking from Booking booking " +
            "where booking.start < ?2 " +
            "and booking.end > ?2 " +
            "and booking.item.owner = ?1 " +
            "order by booking.start desc")
    List<Booking> findAllByOwnerOrderByStartDescCurrent(long ownerId, LocalDateTime now);


    @Query("select booking from Booking booking " +
            "where booking.start < ?2 " +
            "and booking.end < ?2 " +
            "and booking.item.owner = ?1 " +
            "order by booking.start desc")
    List<Booking> findAllByOwnerPast(long ownerId, LocalDateTime now);


    @Query(value = "select booking from Booking booking  " +
            "where booking.item.id = ?1 " +
            "and booking.booker.id = ?2 " +
            "and booking.status like 'APPROVED' " +
            "and booking.end < ?3")
    List<Booking> findLastBookings(Long itemId, Long userId, LocalDateTime now);

    // прошлый и следующий

    // last
    List<Booking> findFirstByItemInAndAndStartBeforeAndStatusEqualsOrderByStartDesc(List<Item> items,
                                                                                    LocalDateTime now,
                                                                                    Status status);

    // next
    List<Booking> findFirstByItemInAndAndStartAfterAndStatusEqualsOrderByStartAsc(List<Item> items,
                                                                                  LocalDateTime now,
                                                                                  Status status);

    // next
    Optional<Booking> findFirstByItemAndStatusLikeAndStartAfterOrderByStartAsc(Item item,
                                                                               Status status,
                                                                               LocalDateTime start);

    // last
    Optional<Booking> findFirstByItemAndStatusLikeAndStartBeforeOrderByStartDesc(Item item,
                                                                                 Status status,
                                                                                 LocalDateTime start);

//    ASC - от большего к меньшему
}