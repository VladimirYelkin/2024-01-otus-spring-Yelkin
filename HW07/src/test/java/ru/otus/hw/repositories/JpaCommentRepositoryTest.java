package ru.otus.hw.repositories;

import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями")
@DataJpaTest
@Import(JpaCommentRepository.class)
class JpaCommentRepositoryTest {

    @Autowired
    private CommentRepository jpaCommentRepository;
    @Autowired
    TestEntityManager testEm;
    @DisplayName(" должен загружать информацию о нужном комментарии по его id")
    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L})
    void shouldFindExpectedCommentById(Long id) {
        var expectedComment = testEm.find(Comment.class, id);

        var actualComment = jpaCommentRepository.findById(id);

        assertThat(actualComment).isPresent();
        assertThat(actualComment.get()).usingRecursiveComparison().isEqualTo(expectedComment);
    }

    @DisplayName(" должен удалять комментарий по id")
    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L,4L})
    void shouldDeleteCommentById(Long id) {
        assertThat(jpaCommentRepository.findById(id)).isPresent();

        jpaCommentRepository.deleteById(id);

        assertThat(jpaCommentRepository.findById(id)).isEmpty();
    }

    @DisplayName(" должен сохранять новый комментарий для книги ")
    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L})
    void shouldSaveNewComment(Long bookId) {
        var expectedComment = new Comment(null, "Comment_for_book"+bookId,  testEm.find(Book.class, bookId));

        var returnedComment = jpaCommentRepository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() != null)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);
        assertThat(testEm.find(Comment.class,returnedComment.getId()))
                .usingRecursiveComparison().isEqualTo(returnedComment);
    }

    @DisplayName(" должен загружать список всех комментариев по Id книги")
    @ParameterizedTest
    @ValueSource(longs = {1L,2L,3L})
    void shouldReturnCorrectCommentListByBook(long bookId) {
        TypedQuery<Comment> query = testEm.getEntityManager().createQuery("select c from Comment c where c.book.id = :bookId ", Comment.class);
        query.setParameter("bookId", bookId);
        var expectedComments = query.getResultList();

        var actualComments = jpaCommentRepository.findByBookId(bookId);

        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
    }
}