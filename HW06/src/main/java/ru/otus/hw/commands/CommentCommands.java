package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    //acbb 2
    @ShellMethod(value = "Find all comment by book id", key = "acbb")
    public String findAllCommentByBookId(long id) {
        return commentService.findAllByBook(id).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    //cbid 3
    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(long id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Book with id %d not found".formatted(id));
    }

    //cins 2 comment_text
    @ShellMethod(value = "Insert comment: cins  <bookId> <textOfComment>", key = "cins")
    public String insertComment(long bookId, String commentText) {
        var savedComment = commentService.insert(bookId, commentText);
        return commentConverter.commentToString(savedComment);
    }

    //cubid 4 comment_text
    @ShellMethod(value = "Update comment by id: cubid <idOfComment> <textofComment>", key = "cubid")
    public String updateComment(long id, String commentText) {
        var savedComment = commentService.update(id, commentText);
        return commentConverter.commentToString(savedComment);
    }

    //cdebid 2
    @ShellMethod(value = "Delete comment by id ", key = "cdel")
    public void deleteComment(long id) {
        commentService.deleteById(id);
    }

}
