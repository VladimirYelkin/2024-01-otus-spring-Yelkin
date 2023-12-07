package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.*;

@NamedEntityGraph(name = "comment-with-book",
        attributeNodes = {@NamedAttributeNode(value = "book", subgraph = "book-sub")},
        subgraphs = @NamedSubgraph(name = "book-sub", attributeNodes = {@NamedAttributeNode("author")}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_text", nullable = false)
    private String fullText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
