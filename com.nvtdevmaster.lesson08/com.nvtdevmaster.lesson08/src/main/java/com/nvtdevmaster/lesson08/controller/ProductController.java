package com.nvtdevmaster.lesson08.controller;

import com.nvtdevmaster.lesson08.entity.*;
import com.nvtdevmaster.lesson08.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ConfigurationService configurationService;
    private final ProductConfigService productConfigService;

    public ProductController(ProductService ps, ConfigurationService cs, ProductConfigService pcs) {
        this.productService = ps;
        this.configurationService = cs;
        this.productConfigService = pcs;
    }

    // LIST
    @GetMapping("")
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products/product-list";
    }

    // CREATE FORM
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("configs", configurationService.findAll());
        return "products/product-form";
    }

    // CREATE PRODUCT
    @PostMapping("/new")
    public String save(
            @ModelAttribute Product product,
            @RequestParam Map<String, String> params
    ) {
        Product saved = productService.save(product);

        params.forEach((key, value) -> {
            if (key.startsWith("config_") && !value.isBlank()) {
                Long configId = Long.valueOf(key.replace("config_", ""));
                ProductConfig pc = new ProductConfig();
                pc.setProduct(saved);
                pc.setConfiguration(configurationService.findById(configId));
                pc.setValue(value);
                productConfigService.save(pc);
            }
        });

        return "redirect:/products";
    }

    // EDIT FORM
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {

        Product product = productService.findById(id);
        List<Configuration> configs = configurationService.findAll();

        model.addAttribute("product", product);
        model.addAttribute("configs", configs);

        return "products/product-form";
    }

    // UPDATE PRODUCT
    @PostMapping("/edit")
    public String update(
            @ModelAttribute Product product,
            @RequestParam Map<String, String> params
    ) {

        // 1. Lưu product đã sửa
        Product updated = productService.save(product);

        // 2. Xóa configs cũ
        updated.getConfigs().forEach(pc -> productConfigService.delete(pc.getId()));

        // 3. Lưu configs mới
        params.forEach((key, value) -> {
            if (key.startsWith("config_") && !value.isBlank()) {
                Long configId = Long.valueOf(key.replace("config_", ""));
                ProductConfig pc = new ProductConfig();
                pc.setProduct(updated);
                pc.setConfiguration(configurationService.findById(configId));
                pc.setValue(value);
                productConfigService.save(pc);
            }
        });

        return "redirect:/products";
    }

    // DELETE PRODUCT
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }
}
