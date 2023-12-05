package ru.otus.hw.repositories;

import ru.otus.hw.models.Comment;

import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findByBookId(long id);

    void deleteById(long id);

    Optional<Comment> findById(long id);
}
