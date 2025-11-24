package com.nvtdevmaster.lab08.controller;

import com.nvtdevmaster.lab08.entity.Book;
import com.nvtdevmaster.lab08.entity.BookAuthor;
import com.nvtdevmaster.lab08.repository.AuthorRepository;
import com.nvtdevmaster.lab08.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final AuthorRepository authorRepo;

    // LIST
    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "book/book_list";
    }

    // ADD
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("title", "Thêm mới sách");
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorRepo.findAll());
        model.addAttribute("selectedAuthors", List.of());
        model.addAttribute("editorId", null);
        return "book/book_form";
    }

    // EDIT
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        if (book == null) return "redirect:/books";

        model.addAttribute("title", "Chỉnh sửa sách");
        model.addAttribute("book", book);
        model.addAttribute("authors", authorRepo.findAll());

        List<Long> selectedAuthors = book.getBookAuthors().stream()
                .map(ba -> ba.getAuthor().getId())
                .collect(Collectors.toList());
        model.addAttribute("selectedAuthors", selectedAuthors);

        Long editorId = book.getBookAuthors().stream()
                .filter(BookAuthor::getIsEditor)
                .map(ba -> ba.getAuthor().getId())
                .findFirst()
                .orElse(null);
        model.addAttribute("editorId", editorId);

        return "book/book_form";
    }

    // SAVE
    @PostMapping("/save")
    public String save(@ModelAttribute Book book,
                       @RequestParam(required = false) List<Long> authorIds,
                       @RequestParam(required = false) Long editorId,
                       @RequestParam(required = false) MultipartFile imageFile) {

        bookService.save(book, authorIds, editorId, imageFile);
        return "redirect:/books";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        bookService.delete(id);
        return "redirect:/books";
    }
}
