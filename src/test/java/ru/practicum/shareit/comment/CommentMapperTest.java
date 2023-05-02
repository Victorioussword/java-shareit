package ru.practicum.shareit.comment;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentMapperTest {

    @Test
    void toCommentTest() {
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        CommentDtoInput commentDtoInput1 = new CommentDtoInput(1l,
                "text comment1",
                user1.getId(),
                user1.getName(),
                LocalDateTime.now());

        Comment comment1 = CommentMapper.toComment(commentDtoInput1, user1, item1);

        assertEquals(comment1.getId(), commentDtoInput1.getId());
        assertEquals(comment1.getText(), commentDtoInput1.getText());
        assertEquals(comment1.getAuthor().getId(), commentDtoInput1.getAuthorId());
        assertEquals(comment1.getAuthor().getName(), commentDtoInput1.getAuthorName());
        assertEquals(comment1.getCreated(), commentDtoInput1.getCreated());
    }


    @Test
    void toCommentDtoInput() {
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Comment comment1 = new Comment(1l, "text comment1", user1, item1, LocalDateTime.now());

    CommentDtoInput commentDtoInput1 = CommentMapper.toCommentDtoInput(comment1);

        assertEquals(comment1.getId(), commentDtoInput1.getId());
        assertEquals(comment1.getText(), commentDtoInput1.getText());
        assertEquals(comment1.getAuthor().getId(), commentDtoInput1.getAuthorId());
        assertEquals(comment1.getAuthor().getName(), commentDtoInput1.getAuthorName());
        assertEquals(comment1.getCreated(), commentDtoInput1.getCreated());
    }

    @Test
    void toCommentDtoOutput() {
        Item item1 = new Item(1L, "item1", "description Item1", true, 1L, null);
        User user1 = new User(1L, "userName", "user@mail.ru");
        Comment comment1 = new Comment(1l, "text comment1", user1, item1, LocalDateTime.now());

        CommentDtoOutput commentDtoOutput1 = CommentMapper.toCommentDtoOutput(comment1);

        assertEquals(comment1.getId(), commentDtoOutput1.getId());
        assertEquals(comment1.getText(), commentDtoOutput1.getText());
        assertEquals(comment1.getAuthor().getId(),commentDtoOutput1.getAuthorId());
        assertEquals(comment1.getAuthor().getName(), commentDtoOutput1.getAuthorName());
        assertEquals(comment1.getCreated(), commentDtoOutput1.getCreated());

    }


}
