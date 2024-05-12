package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dto.CommentDto;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    private final BookConverter bookConverter;

    public String commentToString(CommentDto comment) {
        return "Id: %d, text: %s, book: {%s}".formatted(
                comment.id(),
                comment.fullText(),
                bookConverter.bookWithOutGanresToString(comment.book()));
    }

    public String commentWithoutBookInfoToString(CommentDto comment) {
        return "Id: %d, text: %s".formatted(
                comment.id(),
                comment.fullText());
    }
}
