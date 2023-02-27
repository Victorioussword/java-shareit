package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.Create;

import javax.validation.constraints.NotBlank;
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
        log.info("ItemController - update(). Обновлен {}", itemDto.toString());
        ItemDto itemDtoForReturn = itemService.update(itemDto, itemId, userIdInHeader);
        return itemDtoForReturn;
    }

    @GetMapping("/{id}")
    public ItemDto getById(@PathVariable long id) {
        ItemDto itemDto = itemService.getById(id);
        log.info("ItemController - getById().  Возвращен {}", itemDto.toString());
        return itemDto;
    }

    @GetMapping
    public List<ItemDto> getByOwnerId(@RequestHeader("X-Sharer-User-Id") long ownerId) {
        List<ItemDto> items = itemService.getByOwnerId(ownerId);
        log.info("ItemController - getByOwnerId(). Возвращен список из {} предметов", items.size());
        return items;
    }

    @GetMapping("/search")
    public List<ItemDto> search(@NotBlank @RequestParam String text) {
        List<ItemDto> items = itemService.search(text);
        log.info("ItemController - getByOwnerId(). Возвращен список из {} предметов", items.size());
        return items;
    }
}