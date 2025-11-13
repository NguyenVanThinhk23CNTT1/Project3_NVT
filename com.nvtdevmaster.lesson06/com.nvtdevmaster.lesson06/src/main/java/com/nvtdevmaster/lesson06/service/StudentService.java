package com.nvtdevmaster.lesson06.service;

import com.nvtdevmaster.lesson06.dto.StudentDTO;
import com.nvtdevmaster.lesson06.entity.Student;
import com.nvtdevmaster.lesson06.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repo;

    public List<Student> findAll() {
        return repo.findAll();
    }

    public Optional<StudentDTO> findById(Long id) {
        return repo.findById(id).map(student ->
                new StudentDTO(student.getId(), student.getName(), student.getEmail(), student.getAge())
        );
    }

    public void save(StudentDTO dto) {
        Student s = new Student();
        s.setName(dto.getName());
        s.setEmail(dto.getEmail());
        s.setAge(dto.getAge());
        repo.save(s);
    }

    public void update(Long id, StudentDTO dto) {
        repo.findById(id).ifPresent(student -> {
            student.setName(dto.getName());
            student.setEmail(dto.getEmail());
            student.setAge(dto.getAge());
            repo.save(student);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
