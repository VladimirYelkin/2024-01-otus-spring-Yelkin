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
    public String findAllCommentByBookId(String id) {
        return commentService.findAllByBook(id).stream()
                .map(commentConverter::commentWithoutBookInfoToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    //cbid 3
    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(String id) {
        return commentService.findById(id)
                .map(commentConverter::commentWithoutBookInfoToString)
                .orElse("Comment with id %s not found".formatted(id));
    }

    //cins 2 comment_text
    @ShellMethod(value = "Insert comment: cins  <bookId> <textOfComment>", key = "cins")
    public String insertComment(String bookId, String commentText) {
        var savedComment = commentService.insert(bookId, commentText);
        return commentConverter.commentToString(savedComment);
    }

    //cubid 4 comment_text
    @ShellMethod(value = "Update comment by id: cubid <idOfComment> <textofComment>", key = "cubid")
    public String updateComment(String id, String commentText) {
        var savedComment = commentService.update(id, commentText);
        return commentConverter.commentWithoutBookInfoToString(savedComment);
    }

    //cdebid 2
    @ShellMethod(value = "Delete comment by id ", key = "cdel")
    public void deleteComment(String  id) {
        commentService.deleteById(id);
    }

}
