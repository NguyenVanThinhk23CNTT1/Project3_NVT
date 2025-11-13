package com.nvtdevmaster.lesson06.controller;

import com.nvtdevmaster.lesson06.dto.StudentDTO;
import com.nvtdevmaster.lesson06.entity.Student;
import com.nvtdevmaster.lesson06.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", service.findAll());
        return "students/student-list";
    }

    @GetMapping("/add-new")
    public String addNew(Model model) {
        model.addAttribute("student", new Student());
        return "students/student-add";
    }

    @PostMapping
    public String save(@ModelAttribute("student") StudentDTO dto) {
        service.save(dto);
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        StudentDTO dto = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid student ID:" + id));
        model.addAttribute("student", dto);
        return "students/student-edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @ModelAttribute("student") StudentDTO dto) {
        service.update(id, dto);
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/students";
    }
}
