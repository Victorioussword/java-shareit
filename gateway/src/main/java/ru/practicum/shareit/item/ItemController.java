package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.shareit.item.dto.CommentDtoInput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.Create;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> postItem(@RequestHeader("X-Sharer-User-Id") long userId, @Validated(Create.class) @RequestBody ItemDto itemDto) {
        log.info("Gateway - ItemController - postItem()");
        return itemClient.createItem(itemDto, userId);
    }


    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable long itemId, @RequestHeader("X-Sharer-User-Id") long userId) {

        log.info("Gateway - ItemController - getById(). Возвращен предмет");
        return itemClient.getById(itemId, userId);
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userIdInHeader, @PathVariable long itemId, @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        log.info("Gateway - ItemController - update(). Обновлен");
        return itemClient.update(itemDto, itemId, userIdInHeader);
    }


    @GetMapping
    public ResponseEntity<Object> getAllByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                 @RequestParam(name = "size", defaultValue = "10") @Min(1) @Max(100) Integer size
    ) {
        log.info("Gateway - ItemController - getByOwnerId(). Возвращен список предметов");
        return itemClient.getAllByUserId(userId, from, size);
    }


    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                         @RequestParam(name = "size", defaultValue = "10") @Min(1) @Max(100) Integer size
    ) {
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        } else {

            log.info("ItemController - getByOwnerId(). Возвращен список предметов");
            return itemClient.search(text, from, size);
        }
    }


    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable long itemId,
                                                @Validated({Create.class}) @RequestBody CommentDtoInput comment) {
        return itemClient.createComment(comment, userId, itemId);
    }
}




