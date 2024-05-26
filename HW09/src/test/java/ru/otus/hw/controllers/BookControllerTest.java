package ru.otus.hw.controllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import ru.otus.hw.dto.*;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @Test
    void shouldReturnCorrectBookList() throws Exception {
        var book = new BookDto(1L, "Title_of_book_1",
                new AuthorDto(1L, "Author"),
                List.of(new GenreDto(1L, "Genre")));
        given(bookService.findAll()).willReturn(List.of(book));

        mvc.perform(get("/"))
                .andExpect(view().name("list"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("bookDtoList", Matchers.hasSize(1)))
                .andExpect(content().string(containsString("Title_of_book_1")));
    }

    @Test
    void shouldEditBookById() throws Exception {
        var author1 = new AuthorDto(11L, "Author11");
        var genre1 = new GenreDto(55L, "Genre1");
        var genre2 = new GenreDto(22L, "Genre2");
        var author2 = new AuthorDto(22L, "Author22");
        var titleOfBook = "Title_of_book_Test";
        var book = new BookDto(33L, titleOfBook, author1, List.of(genre2));
        given(bookService.findById(book.id())).willReturn(Optional.of(book));
        given(authorService.findAll()).willReturn(List.of(author1, author2));
        given(genreService.findAll()).willReturn(List.of(genre1, genre2));

        mvc.perform(get("/book/edit").param("id", String.valueOf(book.id())))
                .andExpect(view().name("edit_book"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", Matchers.is(new BookUpdateViewDto(book.id(), book.title(), author1.id(), genre2.id()))))
                .andExpect(model().attribute("authors", Matchers.hasSize(2)))
                .andExpect(model().attribute("genres", Matchers.hasSize(2)))
                .andExpect(content().string(containsString("Edit book")))
                .andExpect(content().string(containsString(titleOfBook)));
    }

    @Test
    void shouldSaveBookAndRedirect() throws Exception {
        var author1 = new AuthorDto(11L, "Author");
        var genre2 = new GenreDto(22L, "Genre2");
        var titleOfBook = "Title_of_book_Test";
        var expectedBookDto = new BookDto(33L, titleOfBook, author1, List.of(genre2));
        var bookUpdateDto = new BookCreateViewDto(
                expectedBookDto.title(),
                expectedBookDto.author().id(),
                genre2.id()
        );

        mvc.perform(post("/book").flashAttr("book", bookUpdateDto))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService, times(1)).insert(bookUpdateDto);
    }

    @Test
    void shouldNotCreateBookWithBlankTitle() throws Exception {
        mvc.perform(post("/book")
                        .param("title", " ")
                        .param("authorId", "33")
                        .param("genreId", "22"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book/create"))
                .andExpect(flash().attributeExists("book", BindingResult.MODEL_KEY_PREFIX + "book"));
    }

    @Test
    void shouldNotCreateBookWithEmptyTitle() throws Exception {
        mvc.perform(post("/book")
                        .param("title", "")
                        .param("authorId", "33")
                        .param("genreId", "22"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book/create"))
                .andExpect(flash().attributeExists("book", BindingResult.MODEL_KEY_PREFIX + "book"));
    }

    @Test
    void shouldDeleteBookWithId() throws Exception {
        mvc.perform(delete("/book/{id}", 3))
                .andExpect(redirectedUrl("/"));
        verify(bookService, only()).deleteById(3);

    }

}