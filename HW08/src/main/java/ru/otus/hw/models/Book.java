package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "book")
public class Book {

    @Id
    private String id;

    private String title;

    @Field
    private Author author;

    private List<Genre> genres;

    public Book(String title, Author author, List<Genre> genres) {
        this(null, title, author, genres);
    }
}
