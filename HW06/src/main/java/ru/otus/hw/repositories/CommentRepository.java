package ru.otus.hw.repositories;

import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    List<Comment> findByBook(long bookId);

    List<Comment> findByBook(Book book);

    void deleteById(long id);

    Optional<Comment> findById(long id);

    Comment save(Comment comment);

}
