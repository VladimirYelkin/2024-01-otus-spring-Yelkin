package ru.otus.hw.controllers;

import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import ru.otus.hw.converters.BookMapper;
import ru.otus.hw.converters.BookMapperImpl;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.assertj.core.api.Assertions.assertThat;

@Import({BookMapperImpl.class})
@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookMapper bookMapper;

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
        given(bookService.findById(book.id())).willReturn(book);
        given(authorService.findAll()).willReturn(List.of(author1, author2));
        given(genreService.findAll()).willReturn(List.of(genre1, genre2));

        mvc.perform(get("/book/edit").param("id", String.valueOf(book.id())))
                .andExpect(view().name("edit_book"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", Matchers.is(new BookUpdateDto(book.id(), book.title(), author1.id(), genre2.id()))))
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
        var bookUpdateDto = new BookCreateDto(
                expectedBookDto.title(),
                expectedBookDto.author().id(),
                genre2.id()
        );

        mvc.perform(post("/book").flashAttr("book", bookUpdateDto))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService, times(1)).save(bookUpdateDto);
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
        verify(bookService, never()).save(any(BookCreateDto.class));
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

    @Test
    void shouldResponse404whenBookNotExists() throws Exception {
        when(bookService.findById(1234L)).thenThrow(new NotFoundException());
        mvc.perform(get("/book/edit").param("id", "1234"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldResponse500whenServicesThrowNpe() throws Exception {
        when(bookService.findById(1234L)).thenThrow(new NullPointerException());
        mvc.perform(get("/book/edit").param("id", "1234"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void shouldNotCreateBookWithoutTitle() throws Exception {
        MvcResult result = mvc.perform(post("/book")
                        .param("genreId", "22"))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book/create"))
                .andExpect(flash().attributeExists("book", BindingResult.MODEL_KEY_PREFIX + "book")).andReturn();

        var binding = (BeanPropertyBindingResult) result.getFlashMap().get("org.springframework.validation.BindingResult.book");
        assertThat(binding.getAllErrors().size()).isEqualTo(2);

        verify(bookService, never()).save(any(BookCreateDto.class));

    }

}
