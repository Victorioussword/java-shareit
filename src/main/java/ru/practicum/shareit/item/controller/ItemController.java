package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentDtoInput;
import ru.practicum.shareit.comment.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.ItemWithBookingAndCommentsDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.Create;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    public List<ItemWithBookingAndCommentsDto> getAllByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                              @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                              @RequestParam(name = "size", defaultValue = "10") @Min(1) @Max(100) Integer size
    ) {
        List<ItemWithBookingAndCommentsDto> items = itemService.getAllByUserId(userId, from, size);
        log.info("ItemController - getByOwnerId(). Возвращен список из {} предметов", items.size());
        return items;
    }


    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text,
                                 @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                @RequestParam(name = "size", defaultValue = "10") @Min(1) @Max(100) Integer size
    ) {
        if (text.isBlank()) {
            return Collections.emptyList();
        } else {
            List<ItemDto> items = itemService.search(text, from, size);
            log.info("ItemController - getByOwnerId(). Возвращен список из {} предметов", items.size());
            return items;
        }
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoOutput createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long itemId,
                                          @Validated({Create.class}) @RequestBody CommentDtoInput comment) {
        return itemService.createComment(comment, userId, itemId);
    }
}