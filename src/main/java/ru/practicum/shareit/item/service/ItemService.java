package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.*;
import ru.practicum.shareit.exception.AvailableCheckException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemWithBookingAndCommentsDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    Sort sort = Sort.by(Sort.Direction.DESC, "id");

    @Transactional
    public ItemDto createItem(ItemDto itemDto, long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("Владелец вещи не найден");
        });
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner.getId());
        ItemDto itemDtoForReturn = ItemMapper.toItemDto(itemRepository.save(item));
        log.info("ItemService - метод createItem (). Добавлен Item {}.", itemDtoForReturn.toString());
        return itemDtoForReturn;
    }

    @Transactional
    public ItemDto update(ItemDto itemDto, long itemId, Long userIdInHeader) {
        Item item = itemRepository.getById(itemId);  // получаем из базы
        checkEqualsUsersIds(item.getOwner(), userIdInHeader);

        prepareItemForUpdate(item, itemDto);
        log.info("ItemService - update(). Обновлен {}", item.toString());
        return ItemMapper.toItemDto(item);
    }

    public List<ItemWithBookingAndCommentsDto> getAllByUserId(long ownerId) {
        List<Item> items = itemRepository.findAllByOwnerOrderByIdAsc(ownerId);
        return addBookingsAndCommentsToList(items);
    }

    public ItemWithBookingAndCommentsDto getById(long id, long userId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item не найден "));  // todo 11
        if (item.getOwner() == userId) {
            log.info("ItemService - getById(). Возвращен {}", item.toString());
            return addBookingsAndComment(item);
        }
        return addComments(item);
    }

    public List<ItemDto> search(String text) {
        List<Item> items = itemRepository.search(text);
        List<ItemDto> itemsDto = items.stream().map(ItemMapper::toItemDto).collect(toList());
        log.info("ItemController - search(). Возвращен список из {} предметов", items.size());
        return itemsDto;
    }

    @Transactional
    public CommentDtoForReturn createComment(CommentDto commentDto, Long userId, Long itemId) {
        commentDto.setCreated(LocalDateTime.now());
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("Пользователь не найден");
        });
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            throw new NotFoundException("Предмет не найден");
        });

        if (bookingRepository.findLastBookings(itemId, userId, LocalDateTime.now()).isEmpty()) {
            throw new AvailableCheckException(
                    " Добавить комментарий не возможно");
        }
        log.info("ItemController -  createComment(). Добавлен комментарий {}", commentDto);
        return CommentMapper.toCommentDtoForReturn(commentRepository.save(CommentMapper.toComment(commentDto, user, item)));
    }


    private Item prepareItemForUpdate(Item item, ItemDto itemDto) {
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        log.info("ItemService - Было {} , Стало {}", item.toString(), itemDto.toString());
        return item;
    }

    private void checkEqualsUsersIds(long idInDb, long userIdInHeader) {
        if (idInDb != userIdInHeader) {
            throw new NotFoundException("Id пользователя не соответствует");
        }
    }

    private ItemWithBookingAndCommentsDto addComments(Item item) {
        ItemWithBookingAndCommentsDto itemWithBookingAndCommentsDto = ItemMapper.toItemWithBookingAndCommentsDto(item);
        itemWithBookingAndCommentsDto.setComments(commentRepository.findAllByItemId(item.getId())
                .stream().map(CommentMapper::toCommentDtoForReturn)
                .collect(toList()));
        return itemWithBookingAndCommentsDto;
    }

    private ItemWithBookingAndCommentsDto addBookingsAndComment(Item item) {
        ItemWithBookingAndCommentsDto itemWithBookingAndCommentsDto = ItemMapper.toItemWithBookingAndCommentsDto(item);

        // получение списка с прошлим и следующим букингом
//        List<Booking> nextBookings = bookingRepository.getNextBookings(item.getId(), LocalDateTime.now());
//        List<Booking> lastBookings = bookingRepository.getLastBookings(item.getId(), LocalDateTime.now());


        Optional<Booking> next = bookingRepository.findFirstByItemAndStatusLikeAndStartAfterOrderByStartAsc(item, Status.APPROVED, LocalDateTime.now());
        Optional<Booking> last =bookingRepository.findFirstByItemAndStatusLikeAndStartBeforeOrderByStartDesc(item, Status.APPROVED, LocalDateTime.now());

        // добавляем в Item прошлый и следующий букинги
        if (next.isPresent()) {
            BookingShortDto bookingShortDtoNext = BookingMapper.toBookingShortDto(next.get());
            itemWithBookingAndCommentsDto.setNextBooking(bookingShortDtoNext);
        }

        if (last.isPresent()) {
            BookingShortDto bookingShortDtoLast =
                    BookingMapper.toBookingShortDto(last.get());
            itemWithBookingAndCommentsDto.setLastBooking(bookingShortDtoLast);
        }

        // добавляем комментарии
        itemWithBookingAndCommentsDto.setComments(commentRepository.findAllByItemId(item.getId())
                .stream().map(CommentMapper::toCommentDtoForReturn)
                .collect(toList()));
        return itemWithBookingAndCommentsDto;
    }

    private List<ItemWithBookingAndCommentsDto> addBookingsAndCommentsToList(List<Item> items) {

        List<ItemWithBookingAndCommentsDto> forReturn = items.stream().map(ItemMapper::toItemWithBookingAndCommentsDto).collect(toList());

        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(items, sort).stream().collect(groupingBy(Comment::getItem, toList()));

        Map<Item, List<Booking>> lasts = bookingRepository.findByItemInAndStartBefore(items, LocalDateTime.now())
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));

        Map<Item, List<Booking>> nexts = bookingRepository.findByItemInAndStartAfter(items, LocalDateTime.now())
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));

        for (int i = 0; i < items.size(); i++) {

            if (!comments.isEmpty() && comments.containsKey(items.get(i))) {
                List<CommentDtoForReturn> comm = comments.get(items.get(i)).stream().map(CommentMapper::toCommentDtoForReturn).collect(toList());
                forReturn.get(i).setComments(comm);
            }
            if (!lasts.isEmpty() && lasts.containsKey(items.get(i))) {
                List<BookingShortDto> lastBookingsShorts = lasts.get(items.get(i)).stream().map(BookingMapper::toBookingShortDto).collect(toList());
                forReturn.get(i).setLastBooking(lastBookingsShorts.get(0));
            }
            if (!nexts.isEmpty() && nexts.containsKey(items.get(i))) {
                List<BookingShortDto> nextBookingsShorts = nexts.get(items.get(i)).stream().map(BookingMapper::toBookingShortDto).collect(toList());
                forReturn.get(i).setNextBooking(nextBookingsShorts.get(nextBookingsShorts.size() - 1));
            }
        }
        return forReturn;
    }
}