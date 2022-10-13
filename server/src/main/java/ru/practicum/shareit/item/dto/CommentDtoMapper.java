package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

public final class CommentDtoMapper {
    public static Comment toComment(CommentDto commentDto) {
        return Comment.builder().text(commentDto.getText())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder().id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static List<CommentDto> toCommentDto(List<Comment> comments) {
        return comments.stream().map(CommentDtoMapper::toCommentDto).collect(Collectors.toList());
    }
}
