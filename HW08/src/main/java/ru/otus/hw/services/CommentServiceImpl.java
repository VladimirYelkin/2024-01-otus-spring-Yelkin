package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAllByBook(String bookId) {
        try {
            Book book = getBookById(bookId);
            return commentRepository.findByBookId(book.getId());
        } catch (EntityNotFoundException e) {
            return  Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public Comment insert(String bookId, String text) {
        var comment = new Comment(text, getBookById(bookId));
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment update(String id, String textOfComment) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
        comment.setFullText(textOfComment);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private Book getBookById(String bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
    }
}
