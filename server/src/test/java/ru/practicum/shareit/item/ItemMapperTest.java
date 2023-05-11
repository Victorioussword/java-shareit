package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.comment.CommentDtoOutput;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemShortForRequest;
import ru.practicum.shareit.item.model.ItemWithBookingAndCommentsDto;
import ru.practicum.shareit.request.model.Request;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemMapperTest {


    void toItemDto() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        ItemDto itemDto1 = ItemMapper.toItemDto(item1);
        assertEquals(item1.getId(), itemDto1.getId());
        assertEquals(item1.getName(), itemDto1.getName());
        assertEquals(item1.getDescription(), itemDto1.getDescription());
        assertEquals(item1.getAvailable(), itemDto1.getAvailable());
    }

    @Test
    void toItemTest() {

        ItemDto itemDto1 = new ItemDto(1L, "item1", "description Item1", true, 1L);
        Item item1 = ItemMapper.toItem(itemDto1);
        assertEquals(item1.getId(), itemDto1.getId());
        assertEquals(item1.getName(), itemDto1.getName());
        assertEquals(item1.getDescription(), itemDto1.getDescription());
        assertEquals(item1.getAvailable(), itemDto1.getAvailable());
    }

    @Test
    void toItemWithBookingAndCommentsDto() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        ArrayList<CommentDtoOutput> arrayList = new ArrayList();
        ItemWithBookingAndCommentsDto withBookingAndCommentsDto1 = ItemMapper.toItemWithBookingAndCommentsDto(item1);
        assertEquals(item1.getId(), withBookingAndCommentsDto1.getId());
        assertEquals(item1.getName(), withBookingAndCommentsDto1.getName());
        assertEquals(item1.getDescription(), withBookingAndCommentsDto1.getDescription());
        assertEquals(item1.getAvailable(), withBookingAndCommentsDto1.getAvailable());
        assertEquals(arrayList.size(), withBookingAndCommentsDto1.getComments().size());
    }

    @Test
    void toItemShortForRequest() {

        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        Request request1 = new Request(1L, "description1", 1L, LocalDateTime.now());
        item1.setRequest(request1);
        ItemShortForRequest itemShortForRequest1 = ItemMapper.toItemShortForRequest(item1);
        assertEquals(item1.getId(), itemShortForRequest1.getId());
        assertEquals(item1.getName(), itemShortForRequest1.getName());
        assertEquals(item1.getDescription(), itemShortForRequest1.getDescription());
        assertEquals(item1.getAvailable(), itemShortForRequest1.getAvailable());
        assertEquals(request1.getId(), itemShortForRequest1.getRequestId());
    }
}