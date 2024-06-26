package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.hw.converters.BookMapper;
import ru.otus.hw.dto.BookCreateDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateDto;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookMapper bookMapper;

    @GetMapping("/")
    public String listPage(Model model) {
        List<BookDto> bookDtoList = bookService.findAll();

        model.addAttribute("bookDtoList", bookDtoList);
        return "list";
    }

    @DeleteMapping("/book/{id}")
    public String deleteBook(@PathVariable("id") long bookId) {
        bookService.deleteById(bookId);
        return "redirect:/";
    }

    @GetMapping("/book/create")
    public String createBook(Model model) {
        if (!model.containsAttribute("book")) {
            model.addAttribute("book", new BookCreateDto());
        }
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "create_book";
    }

    @PostMapping(value = "/book")
    public String saveBook(@Valid @ModelAttribute("book") BookCreateDto bookCreateDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("book", bookCreateDto);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "book", bindingResult);
            return "redirect:/book/create";
        }
        bookService.save(bookCreateDto);
        return "redirect:/";
    }

    @GetMapping("/book/edit")
    public String editBook(@RequestParam("id") long bookId, Model model) {
        if (!model.containsAttribute("book")) {
            var bookDto = bookService.findById(bookId);
            model.addAttribute("book", bookMapper.toDto(bookDto));
        }
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "edit_book";
    }


    @PutMapping("/book/{id}")
    public String updateBook(@PathVariable("id") long bookId,
                             @Valid @ModelAttribute("book") BookUpdateDto bookUpdateDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("book", bookUpdateDto);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "book", bindingResult);
            return "redirect:/book/" + bookId;
        }
        bookService.update(bookUpdateDto);
        return "redirect:/";
    }

}