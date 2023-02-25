package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {


    Item createItem(Item item);

    Item getById(long id);

    Item update(Item item);

    List<Item> getByOwnerId(long id);

    List<Item> search(String text);
}
