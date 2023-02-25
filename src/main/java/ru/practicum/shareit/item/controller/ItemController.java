package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.Create;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public Item postItem(@NotNull @RequestHeader("X-Sharer-User-Id") long userId, @Validated({Create.class}) @RequestBody ItemDto itemDto) {
        log.info("ItemController - postItem().  ДОбавлен  {}", itemDto.toString());
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public Item update(@NotNull @RequestHeader("X-Sharer-User-Id") Long userIdInHeader, @PathVariable long itemId, @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        log.info("ItemController - update(). Обновлен {}", itemDto.toString());
        Item itemForReturn = itemService.update(itemDto, itemId, userIdInHeader);
        return itemForReturn;
    }

    @GetMapping("/{id}")
    public Item getById(@PathVariable long id) {
        Item item = itemService.getById(id);
        log.info("ItemController - getById().  Возвращен {}", item.toString());
        return item;
    }

    @GetMapping
    public List<Item> getByOwnerId(@NotNull @RequestHeader("X-Sharer-User-Id") long ownerId) {
        List<Item> items = itemService.getByOwnerId(ownerId);
        log.info("ItemController - getByOwnerId(). Возвращен список из {} предметов", items.size());
        return items;
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam String text) {
        List<Item> items = itemService.search(text);
        log.info("ItemController - getByOwnerId(). Возвращен список из {} предметов", items.size());
        return items;
    }
}