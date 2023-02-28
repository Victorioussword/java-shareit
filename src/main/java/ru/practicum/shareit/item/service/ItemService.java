package ru.practicum.shareit.item.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto createItem(ItemDto itemDto, long userId) {
        checkExistUser(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwnerId(userId);
        ItemDto itemDtoForReturn = ItemMapper.toItemDto(itemRepository.createItem(item));
        log.info("ItemService - метод createItem (). Добавлен Item {}.", itemDtoForReturn.toString());
        return itemDtoForReturn;
    }


    public ItemDto getById(long id) {
        ItemDto itemDtoForReturn = ItemMapper.toItemDto(itemRepository.getById(id));
        log.info("ItemService - getById(). Добавлен Item {}.", itemDtoForReturn.toString());
        return itemDtoForReturn;
    }


    public ItemDto update(ItemDto itemDto, long itemId, Long userIdInHeader) {
        Item item = itemRepository.getById(itemId);
        checkEqualsUsersIds(item.getOwnerId(), userIdInHeader);
        checkExistUser(userIdInHeader);
        prepareItemForUpdate(item, itemDto);
        ItemDto itemDtoForReturn = ItemMapper.toItemDto(item);
        log.info("ItemService - update(). Обновлен {}", item.toString());
        return itemDtoForReturn;
    }

    private Item prepareItemForUpdate(Item item, ItemDto itemDto) {
        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());  // проблема с доступностью
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

    public List<ItemDto> getByOwnerId(long ownerId) {
        List<Item> items = itemRepository.getByOwnerId(ownerId);
        log.info("ItemController - getByOwnerId(). Возвращен список из {} предметов", items.size());
        List<ItemDto> itemsDto = items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        return itemsDto;
    }

    public List<ItemDto> search(String text) {
        List<Item> items = itemRepository.search(text);
        List<ItemDto> itemsDto = items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
        log.info("ItemController - search(). Возвращен список из {} предметов", items.size());
        return itemsDto;
    }
}