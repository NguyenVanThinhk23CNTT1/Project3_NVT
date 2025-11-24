package com.nvtdevmaster.lesson08.controller;

import com.nvtdevmaster.lesson08.entity.Author;
import com.nvtdevmaster.lesson08.entity.Book;
import com.nvtdevmaster.lesson08.service.AuthorService;
import com.nvtdevmaster.lesson08.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    // Thư mục upload trong project: src/main/resources/static/images
    private static final String UPLOAD_DIR = "C:/Users/ADMIN/OneDrive/Hình ảnh/";
    private static final String UPLOAD_URL = "/images/";   // đường load ảnh

    // ===================== DANH SÁCH =====================
    @GetMapping("")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books/book-list";
    }

    // ===================== FORM TẠO MỚI =====================
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authorService.getAllAuthors());
        return "books/book-form";
    }

    // ===================== LƯU BOOK =====================
    @PostMapping("/new")
    public String saveBook(@ModelAttribute Book book,
                           @RequestParam(required = false) List<Long> authorIds,
                           @RequestParam("imageBook") MultipartFile imageFile) {

        // Nếu user upload ảnh mới
        if (!imageFile.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String originalName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                String ext = originalName.substring(originalName.lastIndexOf(".")); // .jpg

                String fileName = book.getCode() + ext; // ví dụ: B001.jpg

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(),
                        filePath,
                        StandardCopyOption.REPLACE_EXISTING);

                book.setImgUrl(UPLOAD_URL + fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // GÁN TÁC GIẢ
        List<Author> authors = new ArrayList<>();
        if (authorIds != null) {
            authors = authorService.findAllById(authorIds);
        }

        book.setAuthors(authors);

        bookService.saveBook(book);

        return "redirect:/books";
    }

    // ===================== FORM EDIT =====================
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);

        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.getAllAuthors());

        return "books/book-form";
    }

    // ===================== DELETE =====================
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}
