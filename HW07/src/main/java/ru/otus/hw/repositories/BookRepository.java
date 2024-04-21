package ru.otus.hw.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph("book-graph")
    Optional<Book> findById( Long id);


    @Override
    @EntityGraph("book-graph-without-genres")
    List<Book> findAll();
}
