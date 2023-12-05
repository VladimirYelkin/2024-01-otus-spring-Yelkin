package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;

    public JpaCommentRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Comment> findByBookId(long id) {
        return Optional.empty();
    }

    @Override
    public void deleteById(long id) {
        em.remove(findById(id).get());
    }

    @Override
    public Optional<Comment> findById(long id) {
        EntityGraph<?> graph = em.getEntityGraph("comment-with-book");
        Map<String, Object> properties = new HashMap<>();
        properties.put(FETCH.getKey(), graph);
        return Optional.ofNullable(em.find(Comment.class, id, properties));
    }
}
