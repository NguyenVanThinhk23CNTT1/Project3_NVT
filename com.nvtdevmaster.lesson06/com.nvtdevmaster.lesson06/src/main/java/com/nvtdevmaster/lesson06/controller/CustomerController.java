package com.nvtdevmaster.lesson06.controller;

import com.nvtdevmaster.lesson06.dto.CustomerDTO;
import com.nvtdevmaster.lesson06.entity.Customer;
import com.nvtdevmaster.lesson06.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("customers", service.findAll());
        return "customers/customer-list";
    }

    @GetMapping("/add-new")
    public String addPage(Model model) {
        model.addAttribute("customer", new Customer());
        return "customers/customer-add";
    }

    @PostMapping
    public String save(@ModelAttribute("customer") CustomerDTO dto) {
        service.save(dto);
        return "redirect:/customers";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        CustomerDTO dto = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid customer ID:" + id));
        model.addAttribute("customer", dto);
        return "customers/customer-edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute CustomerDTO dto) {
        service.update(id, dto);
        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/customers";
    }
}
