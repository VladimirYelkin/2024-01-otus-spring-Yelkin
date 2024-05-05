package ru.otus.hw.dto;

import ru.otus.hw.models.Comment;

public record CommentDto(Long id,String fullText,BookDto book) {
    public CommentDto(Comment comment, BookDto bookDto) {
        this(comment.getId(), comment.getFullText(), bookDto);
    }
}
