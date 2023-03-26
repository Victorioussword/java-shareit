package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForReturn;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.AvailableCheckException;
import ru.practicum.shareit.exception.NotExistInDataBase;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnknownStatusException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public BookingDtoForReturn postBooking(BookingDto bookingDto) {
        Optional<User> booker = userRepository.findById(bookingDto.getBooker());
        if (booker.isEmpty()) {
            throw new NotExistInDataBase("Booker не обнаружен в базе данных");  //TODO проверить код ответа
        }

        Optional<Item> item = itemRepository.findById(bookingDto.getItemId());
        if (item.isEmpty()) {
            throw new NotFoundException("Item не обнаружен в базе данных");
        }

        if (booker.get().getId() == item.get().getOwner()) {
            throw new NotFoundException("Владелец не может бронировать свою вещь");
        }
        checkAvailable(item.orElseThrow());

        checkTimeCreate(BookingMapper.toBooking(bookingDto, booker.get(), item.get()));
        Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingDto, booker.get(), item.get()));
        booking.setStatus(Status.WAITING.toString());
        log.info("BookingService - postBooking().  ДОбавлено  {}", booking.toString());
        return BookingMapper.toBookingDtoForReturn(bookingRepository.save(booking));
    }

    private void checkAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new AvailableCheckException("Вещь в данный момент не доступна");
        }
    }

    public BookingDtoForReturn approving(long bookingId, long userId, Boolean status) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> {
            throw new NotFoundException("Booking не найден");
        });

        Item item = itemRepository.getById(booking.getItem().getId()); // получили из репозитория

        checkOwner(userId, item.getOwner());

        if (status == null) throw new IllegalArgumentException("Status не может быть null! Укажите статус");

        if (item.getOwner() != userId) {
            throw new NotFoundException("Данные User не может подтвердить бронирование");
        }
        if (booking.getStatus().equals(Status.APPROVED.toString())) {
            throw new AvailableCheckException("Бронирование подтверждено ранее. Изменение больше не доступно.");
        }
        if (status) {
            booking.setStatus(Status.APPROVED.toString());
        } else booking.setStatus(Status.REJECTED.toString());
        log.info("BookingService - approving().  Подтверждено  {}", booking.toString());
        checkTimeUpdate(booking);
        return BookingMapper.toBookingDtoForReturn(bookingRepository.save(booking));
    }


    private void checkTimeCreate(Booking booking) {
        if (booking.getEnd().isBefore(booking.getStart()) ||
                booking.getEnd().isEqual(booking.getStart()) ||
                booking.getEnd().isBefore(LocalDateTime.now()) ||
                booking.getStart().isBefore(LocalDateTime.now())) {
            throw new AvailableCheckException("Период бронирования задан не корректно");
        }
    }

    private void checkTimeUpdate(Booking booking) {
        if (booking.getEnd().isBefore(booking.getStart()) || booking.getEnd().isEqual(booking.getStart())) {
            throw new AvailableCheckException("Период бронирования задан не корректно");
        }
    }

    private void checkOwner(long userId, long ownerId) {
        if (userId != ownerId) {
            throw new NotFoundException("Данные User не может подтвердить бронирование");
        }
    }

    public BookingDtoForReturn getById(long id, long userId) {
        Optional<Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            throw new NotFoundException("Бронирование отсутствует");
        }
        if (booking.get().getItem().getOwner() != userId && booking.get().getBooker().getId() != userId) {
            throw new NotFoundException("Юзеру не доступна информация о бронировании");
        }
        BookingDtoForReturn bookingDtoForReturn = BookingMapper.toBookingDtoForReturn(booking.get());
        log.info("BookingService - getById(). Возвращено  {}", booking.toString());
        return bookingDtoForReturn;
    }

    public List<BookingDtoForReturn> getByBookerId(long userId, String state) {  // TODO раскоментировать после отладки
        Optional<User> booker = userRepository.findById(userId);
        if (booker.isEmpty()) {
            throw new NotExistInDataBase("User не существует");
        }
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBookerOrderByStartDesc(userId).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case "FUTURE":
                return bookingRepository.findAllByBookerOrderByStartDescFuture(userId, LocalDateTime.now()).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case "WAITING":
                return bookingRepository.findAllByBookerOrderByStartDescStatus(userId, "WAITING").
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case "REJECTED":
                return bookingRepository.findAllByBookerOrderByStartDescStatus(userId, "REJECTED").
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case "CURRENT":
                return bookingRepository.findAllByBookerOrderByStartDescCurrent(userId, LocalDateTime.now()).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case "PAST":
                return bookingRepository.findAllByBookerPast(userId, LocalDateTime.now()).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());
            default:
                throw new UnknownStatusException("Unknown state: " + state);
        }
    }

    public List<BookingDtoForReturn> getByOwnerId(long ownerId, String state) {  // TODO раскоментировать после отладки
        Optional<User> owner = userRepository.findById(ownerId);
        if (owner.isEmpty()) {
            throw new NotExistInDataBase("User не существует");
        }
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByOwnerOrderByStartDesc(ownerId).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case "FUTURE":
                return bookingRepository.findAllByOwnerOrderByStartDescFuture(ownerId, LocalDateTime.now()).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case "WAITING":
                return bookingRepository.findAllByOwnerOrderByStartDescStatus(ownerId, "WAITING").
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case "REJECTED":
                return bookingRepository.findAllByOwnerOrderByStartDescStatus(ownerId, "REJECTED").
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case "CURRENT":
                return bookingRepository.findAllByOwnerOrderByStartDescCurrent(ownerId, LocalDateTime.now()).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case "PAST":
                return bookingRepository.findAllByOwnerPast(ownerId, LocalDateTime.now()).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());
            default:
                throw new UnknownStatusException("Unknown state: " + state);
        }
    }
}



