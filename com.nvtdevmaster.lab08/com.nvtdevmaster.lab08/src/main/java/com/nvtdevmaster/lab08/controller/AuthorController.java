package com.nvtdevmaster.lab08.controller;

import com.nvtdevmaster.lab08.entity.Author;
import com.nvtdevmaster.lab08.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepo;

    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size,
                       @RequestParam(defaultValue = "") String keyword) {

        Page<Author> pageAuthors =
                authorRepo.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(
                        keyword, keyword, PageRequest.of(page, size));

        model.addAttribute("authors", pageAuthors.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageAuthors.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "author/author_list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("title", "Thêm tác giả mới");
        model.addAttribute("author", new Author());
        return "author/author_form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Author author = authorRepo.findById(id).orElse(null);
        if (author == null) return "redirect:/authors";
        model.addAttribute("title", "Chỉnh sửa tác giả");
        model.addAttribute("author", author);
        return "author/author_form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Author author) {
        authorRepo.save(author);
        return "redirect:/authors";
    }

    // Save từ modal trong book_form -> sau khi thêm xong reload lại trang book/add
    @PostMapping("/save-from-book")
    public String saveFromBook(@ModelAttribute Author author) {
        authorRepo.save(author);
        return "redirect:/books/add";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        authorRepo.deleteById(id);
        return "redirect:/authors";
    }
}
