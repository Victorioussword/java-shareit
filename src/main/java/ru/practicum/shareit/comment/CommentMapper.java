package ru.practicum.shareit.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(CommentDto commentDto, User author, Item item) {
        return new Comment(commentDto.getId(),
                commentDto.getText(),
                author,
                item,
                commentDto.getCreated());
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getText(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }
    public static CommentDtoForReturn toCommentDtoForReturn(Comment comment) {
        return new CommentDtoForReturn(comment.getId(),
                comment.getText(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getCreated());
    }

}
