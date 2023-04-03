package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentDtoForReturn;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingAndCommentsDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.Create;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto postItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("ItemController - postItem().  ДОбавлен  {}", itemDto.toString());
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userIdInHeader, @PathVariable long itemId, @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        ItemDto itemDtoForReturn = itemService.update(itemDto, itemId, userIdInHeader);
        log.info("ItemController - update(). Обновлен {}", itemDtoForReturn);
        return itemDtoForReturn;
    }

    @GetMapping("/{itemId}")
    public ItemWithBookingAndCommentsDto getById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {
        ItemWithBookingAndCommentsDto item = itemService.getById(itemId, userId);
        log.info("ItemController - getById(). Возвращен предмет {} предметов", item.toString());
        return item;
    }

    @GetMapping
    public List<ItemWithBookingAndCommentsDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        List<ItemWithBookingAndCommentsDto> items = itemService.getAllByUserId(userId);
        log.info("ItemController - getByOwnerId(). Возвращен список из {} предметов", items.size());
        return items;
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        } else {
            List<ItemDto> items = itemService.search(text);
            log.info("ItemController - getByOwnerId(). Возвращен список из {} предметов", items.size());
            return items;
        }
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoForReturn createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @Validated({Create.class}) @RequestBody CommentDto comment) {
        return itemService.createComment(comment, userId, itemId);
    }
}