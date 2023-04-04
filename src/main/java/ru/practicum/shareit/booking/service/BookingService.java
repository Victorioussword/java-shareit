package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForReturn;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
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
@Transactional(readOnly = true)
public class BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    Sort sort = Sort.by(Sort.Direction.DESC, "start");

    @Transactional
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
        booking.setStatus(Status.WAITING);  // TODO 6
        log.info("BookingService - postBooking().  ДОбавлено  {}", booking.toString());
        return BookingMapper.toBookingDtoForReturn(bookingRepository.save(booking));
    }

    private void checkAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new AvailableCheckException("Вещь в данный момент не доступна");
        }
    }

    @Transactional
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
        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new AvailableCheckException("Бронирование подтверждено ранее. Изменение больше не доступно.");
        }
        if (status) {
            booking.setStatus(Status.APPROVED);
        } else booking.setStatus(Status.REJECTED);
        log.info("BookingService - approving().  Подтверждено  {}", booking.toString());
        checkTimeUpdate(booking);
        return BookingMapper.toBookingDtoForReturn(booking);  // Вызов метода save() больше не требуется. return BookingMapper.toBookingDtoForReturn(bookingRepository.save(booking));
    }


    private void checkTimeCreate(Booking booking) {
        if (booking.getEnd().isBefore(booking.getStart()) ||
                booking.getEnd().isEqual(booking.getStart())) {
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
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Бронирование отсутствует");
        });
        if (booking.getItem().getOwner() != userId && booking.getBooker().getId() != userId) {
            throw new NotFoundException("Юзеру не доступна информация о бронировании");
        }
        BookingDtoForReturn bookingDtoForReturn = BookingMapper.toBookingDtoForReturn(booking);
        log.info("BookingService - getById(). Возвращено  {}", booking.toString());
        return bookingDtoForReturn;
    }

    public List<BookingDtoForReturn> getByBookerId(long userId, State state) {  // TODO раскоментировать после отладки
        if (!userRepository.existsById(userId)) {
            throw new NotExistInDataBase("User не существует");
        }
        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerOrderByStartDesc(userId).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case FUTURE:
                return bookingRepository.findAllByBookerOrderByStartDescFuture(userId, LocalDateTime.now(), sort).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case WAITING:
                return bookingRepository.findAllByBookerOrderByStartDescStatus(userId, Status.WAITING).  // todo 6
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case REJECTED:
                return bookingRepository.findAllByBookerOrderByStartDescStatus(userId, Status.REJECTED).  // todo 6
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case CURRENT:
                return bookingRepository.findAllByBookerOrderByStartDescCurrent(userId, LocalDateTime.now()).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case PAST:
                return bookingRepository.findAllByBookerPast(userId, LocalDateTime.now()).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());
            default:
                throw new UnknownStatusException("Unknown state: " + "UNSUPPORTED_STATUS");
        }
    }

    public List<BookingDtoForReturn> getByOwnerId(long ownerId, State state) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotExistInDataBase("User не существует");
        }
        switch (state) {
            case ALL:
                return bookingRepository.findAllByOwnerOrderByStartDesc(ownerId).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case FUTURE:
                return bookingRepository.findAllByOwnerOrderByStartDescFuture(ownerId, LocalDateTime.now()).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case WAITING:
                return bookingRepository.findAllByOwnerOrderByStartDescStatus(ownerId, Status.WAITING).  // todo 6
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case REJECTED:
                return bookingRepository.findAllByOwnerOrderByStartDescStatus(ownerId, Status.REJECTED).  // todo 6
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case CURRENT:
                return bookingRepository.findAllByOwnerOrderByStartDescCurrent(ownerId, LocalDateTime.now()).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());

            case PAST:
                return bookingRepository.findAllByOwnerPast(ownerId, LocalDateTime.now()).
                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());
            default:
                throw new UnknownStatusException("Unknown state: " + "UNSUPPORTED_STATUS");
        }
    }
}

//  case "WAITING":
//                return bookingRepository.findAllByOwnerOrderByStartDescStatus(ownerId, "WAITING").  //
//                        stream().map(BookingMapper::toBookingDtoForReturn).collect(Collectors.toList());


