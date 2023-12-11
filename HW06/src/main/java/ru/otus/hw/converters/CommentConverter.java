package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    private final BookConverter bookConverter;

    public String commentToString(Comment comment) {
        return "Id: %d, text: %s, book: {%s}".formatted(
                comment.getId(),
                comment.getFullText(),
                bookConverter.bookWithOutGanresToString(comment.getBook()));
    }

    public String commentWithoutBookInfoToString(Comment comment) {
        return "Id: %d, text: %s".formatted(
                comment.getId(),
                comment.getFullText());
    }
}
