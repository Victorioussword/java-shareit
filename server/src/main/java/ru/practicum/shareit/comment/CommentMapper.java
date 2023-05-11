package ru.practicum.shareit.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(CommentDtoInput commentDto, User author, Item item) {
        return new Comment(commentDto.getId(),
                commentDto.getText(),
                author,
                item,
                commentDto.getCreated());
    }

    public static CommentDtoInput toCommentDtoInput(Comment comment) {
        return new CommentDtoInput(comment.getId(),
                comment.getText(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

    public static CommentDtoOutput toCommentDtoOutput(Comment comment) {
        return new CommentDtoOutput(comment.getId(),
                comment.getText(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }
}