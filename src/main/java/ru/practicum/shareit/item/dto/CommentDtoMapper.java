package ru.practicum.shareit.item.dto;

import net.bytebuddy.asm.Advice;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public final class CommentDtoMapper {
    public static Comment toComment(CommentDto commentDto) {
        return Comment.builder().text(commentDto.getText())
                .build();
    }
}
