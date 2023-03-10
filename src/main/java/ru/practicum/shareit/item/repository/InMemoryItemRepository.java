package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class InMemoryItemRepository implements ItemRepository {
    private long id = 1;

    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item createItem(Item item) {
        item.setId(id);
        id++;
        items.put(item.getId(), item);
        log.info("InMemoryItemRepository - метод createItem(). Создан {} ", item.toString());
        return item;
    }

    @Override
    public Item getById(long id) {
        if (items.containsKey(id)) {
            Item itemForReturn = items.get(id);
            log.info("InMemoryItemRepository - метод getById(). Возвращен {} ", itemForReturn.toString());
            return itemForReturn;
        } else {
            log.info("InMemoryItemRepository - метод getById(). Item {} не найден.", id);
            throw new NotFoundException("Item не найден");
        }
    }

    public Map<Long, Item> getItems() {
        return Map.copyOf(items);
    }

    @Override
    public Item update(Item item) {
        log.info("InMemoryItemRepository - метод update(). Обновлен {} ", item.toString());
        items.put(item.getId(), item);
        return item;
    }

    public List<Item> getByOwnerId(long id) {
        return items.values().stream().filter(i -> i.getOwnerId() == id).collect(Collectors.toList());
    }

    public List<Item> search(String text) {
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }
}