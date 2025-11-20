package com.nvtdevmaster.lesson08.controller;

import com.nvtdevmaster.lesson08.entity.Author;
import com.nvtdevmaster.lesson08.service.AuthorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    // LIST
    @GetMapping("")
    public String listAuthors(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        return "authors/author-list";
    }

    // CREATE FORM
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("author", new Author());
        return "authors/author-form";
    }

    // SAVE
    @PostMapping("/new")
    public String save(@ModelAttribute Author author) {
        authorService.saveAuthor(author);
        return "redirect:/authors";
    }

    // EDIT FORM
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("author", authorService.getAuthorById(id));
        return "authors/author-form";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return "redirect:/authors";
    }
}
