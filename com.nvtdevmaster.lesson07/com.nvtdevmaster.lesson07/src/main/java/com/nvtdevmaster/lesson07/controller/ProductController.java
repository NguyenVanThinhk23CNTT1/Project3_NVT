package com.nvtdevmaster.lesson07.controller;

import com.nvtdevmaster.lesson07.entity.Product;
import com.nvtdevmaster.lesson07.repository.CategoryRepository;
import com.nvtdevmaster.lesson07.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    // LIST
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product/product-list";
    }

    // ADD FORM
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/product-form";
    }

    // SAVE (ĐÃ FIX)
    @PostMapping("/save")
    public String save(@ModelAttribute Product product) {

        // Load category theo ID từ form
        var category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Gán lại category đầy đủ
        product.setCategory(category);

        productService.save(product);
        return "redirect:/products";
    }

    // EDIT FORM
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/product-form";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }
}
