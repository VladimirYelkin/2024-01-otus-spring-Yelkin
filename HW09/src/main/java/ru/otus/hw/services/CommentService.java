package ru.otus.hw.services;

import ru.otus.hw.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto findById(long id);

    List<CommentDto> findAllByBook(long bookId);

    CommentDto insert(long bookId, String comment);

    CommentDto update(long id, String comment);

    void deleteById(long id);
}
