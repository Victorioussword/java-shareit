package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AvailableCheckException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.InMemoryItemRepository;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ItemService {
    private final InMemoryItemRepository itemRepository;
    private final InMemoryUserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(ItemService.class);

    public Item createItem(ItemDto itemDto, long userId) {
        checkExistUser(userId);
        itemDto.setOwnerId(userId);
        Item item = ItemMapper.toItem(itemDto);
        if (!item.getAvailable()) {
            throw new AvailableCheckException("Укажите доступность Item");
        }
        if (item.getName().isEmpty()) {
            throw new AvailableCheckException("Укажите Name");
        }
        if (item.getDescription().isEmpty()) {
            throw new AvailableCheckException("Укажите Description");
        }
        log.info("ItemService - метод createItem (). Добавлен Item {}.", item.toString());
        return itemRepository.createItem(item);
    }

    public Item getById(long id) {
        checkExistItem(id);
        Item item = itemRepository.getById(id);
        log.info("ItemService - метод createItem (). Добавлен Item {}.", item.toString());
        return item;
    }

    public Item update(ItemDto itemDto, long itemId, Long userIdInHeader) {
        Item item = itemRepository.getById(itemId);
        checkEqualsUsersIds(item.getOwnerId(), userIdInHeader);
        checkExistUser(userIdInHeader);
        checkExistItem(itemId);
        log.info("ItemService - update(). Обновлен {}", item.toString());
        prepareItemForUpdate(item, itemDto);
        return itemRepository.update(item);
    }

    private Item prepareItemForUpdate(Item item, ItemDto itemDto) {
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null && item.getAvailable() != itemDto.getAvailable()) {
            item.setAvailable(itemDto.getAvailable());  // проблема с доступностью
        }
        if (itemDto.getOwnerId() != null && itemDto.getOwnerId() != item.getOwnerId()) {
            item.setOwnerId(itemDto.getOwnerId());
        }
        log.info("ItemService - Было {} , Стало {}", item.toString(), itemDto.toString());
        return item;
    }


    private void checkEqualsUsersIds(long idInDb, long userIdInHeader) {
        if (idInDb != userIdInHeader) {
            throw new NotFoundException("Id пользователя не соответствует");
        }
    }

    private void checkExistUser(long id) {
        if (!userRepository.getUsers().containsKey(id)) {
            throw new NotFoundException("Пoльзователь не существует");
        }
    }

    private void checkExistItem(long id) {
        if (!itemRepository.getItems().containsKey(id)) {
            throw new NotFoundException("Инструмент не существует");
        }
    }

    public List<Item> getByOwnerId(long ownerId) {
        List<Item> items = itemRepository.getByOwnerId(ownerId);
        log.info("ItemController - getByOwnerId(). Возвращен список из {} предметов", items.size());
        return items;
    }

    public List<Item> search(String text) {
        List<Item> items = itemRepository.search(text);
        log.info("ItemController - search(). Возвращен список из {} предметов", items.size());
        return items;
    }
}