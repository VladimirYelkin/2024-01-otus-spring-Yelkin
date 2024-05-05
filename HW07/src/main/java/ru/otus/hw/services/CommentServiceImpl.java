package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(long id) {
        return commentRepository.findWithBookById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByBook(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment insert(long bookId, String comment) {
        return commentRepository.save(new Comment(null, comment, getBook(bookId)));
    }

    @Override
    @Transactional
    public Comment update(long id, String comment) {
        return commentRepository.findById(id)
                .map(commentUpdated -> {
                    commentUpdated.setFullText(comment);
                    return commentRepository.save(commentUpdated);
                })
                .orElseThrow(() -> new EntityNotFoundException("comment with id %d not found".formatted(id)));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Book getBook(long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("book with id %d not found".formatted(bookId)));
    }
}
