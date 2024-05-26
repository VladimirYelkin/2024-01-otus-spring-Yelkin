package ru.otus.hw.controllers;

import jakarta.validation.Valid;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.otus.hw.converters.BookConverter;
import ru.otus.hw.dto.BookCreateViewDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookUpdateViewDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookConverter bookConverter;

    @GetMapping("/")
    public String listPage(Model model) {
        List<BookDto> bookDtoList = bookService.findAll();

        model.addAttribute("bookDtoList", bookDtoList);
        return "list";
    }

    @DeleteMapping("/book/{id}")
    public ModelAndView deleteBook(@PathVariable("id") long bookId) {
        bookService.deleteById(bookId);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/book/create")
    public String createBook(Model model) {
        if (!model.containsAttribute("book")) {
            model.addAttribute("book", new BookCreateViewDto());
        }
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "create_book";
    }

    @PostMapping(value = "/book")
    public ModelAndView saveBook(@Valid @ModelAttribute("book") BookCreateViewDto bookCreateViewDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("book", bookCreateViewDto);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "book", bindingResult);
            return new ModelAndView("redirect:/book/create");
        }
        bookService.insert(bookCreateViewDto);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/book/edit")
    public String editBook(@RequestParam("id") long bookId, Model model) {
        if (!model.containsAttribute("book")) {
            var bookDto = bookService.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));
            model.addAttribute("book", bookConverter.convert(bookDto));
        }
        model.addAttribute("genres", genreService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "edit_book";
    }

    @PutMapping("/book/{id}")
    public ModelAndView updateBook(@PathVariable("id") long bookId,
                                   @Valid @ModelAttribute("book") BookUpdateViewDto bookUpdateViewDto,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("book", bookUpdateViewDto);
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "book", bindingResult);
            return new ModelAndView("redirect:/book/" + bookId);
        }
        bookService.update(bookUpdateViewDto);
        return new ModelAndView("redirect:/");
    }

}