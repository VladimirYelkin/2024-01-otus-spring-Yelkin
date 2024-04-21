//package ru.otus.hw.repositories;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.TypedQuery;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//import ru.otus.hw.models.Genre;
//
//import java.util.Collection;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Repository
//@RequiredArgsConstructor
//public class JpaGenreRepository implements GenreRepository {
//
//    @PersistenceContext
//    private final EntityManager em;
//
//    @Override
//    public Set<Genre> findAll() {
//        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
//        return query.getResultStream().collect(Collectors.toSet());
//    }
//
//    @Override
//    public Set<Genre> findAllByIds(Collection<Long> ids) {
//        TypedQuery<Genre> query = em.createQuery("select g from Genre g where g.id in :ids", Genre.class);
//        query.setParameter("ids",ids);
//        return query.getResultStream().collect(Collectors.toSet());
//    }
//}
