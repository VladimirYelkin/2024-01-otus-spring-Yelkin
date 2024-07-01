package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public CommentDto findById(long id) {
        return commentRepository.findById(id).map(CommentDto::new)
                .orElseThrow(() -> new NotFoundException("comment with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByBook(long bookId) {
        return commentRepository.findByBookId(bookId).stream().map(CommentDto::new).toList();
    }

    @Override
    @Transactional
    public CommentDto insert(long bookId, String comment) {
        var savedComment = commentRepository.save(new Comment(null, comment, getBook(bookId)));
        return new CommentDto(savedComment, new BookDto(savedComment.getBook()));
    }

    @Override
    @Transactional
    public CommentDto update(long id, String comment) {
        return commentRepository.findById(id)
                .map(commentUpdated -> {
                    commentUpdated.setFullText(comment);
                    return new CommentDto(commentRepository.save(commentUpdated));
                })
                .orElseThrow(() -> new NotFoundException("comment with id %d not found".formatted(id)));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Book getBook(long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("book with id %d not found".formatted(bookId)));
    }
}
