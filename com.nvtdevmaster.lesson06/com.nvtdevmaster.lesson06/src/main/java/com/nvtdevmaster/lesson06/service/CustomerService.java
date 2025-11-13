package com.nvtdevmaster.lesson06.service;

import com.nvtdevmaster.lesson06.dto.CustomerDTO;
import com.nvtdevmaster.lesson06.entity.Customer;
import com.nvtdevmaster.lesson06.repository.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repo;

    public List<Customer> findAll() {
        return repo.findAll();
    }

    public Optional<CustomerDTO> findById(Long id) {
        return repo.findById(id).map(c ->
                new CustomerDTO(
                        c.getId(),
                        c.getUsername(),
                        c.getPassword(),
                        c.getFullName(),
                        c.getAddress(),
                        c.getPhone(),
                        c.getEmail(),
                        c.getBirthDay(),
                        c.isActive()
                ));
    }

    public void save(CustomerDTO dto) {
        Customer c = new Customer();
        c.setUsername(dto.getUsername());
        c.setPassword(dto.getPassword());
        c.setFullName(dto.getFullName());
        c.setAddress(dto.getAddress());
        c.setPhone(dto.getPhone());
        c.setEmail(dto.getEmail());
        c.setBirthDay(dto.getBirthDay());
        c.setActive(dto.isActive());
        repo.save(c);
    }

    public void update(Long id, CustomerDTO dto) {
        repo.findById(id).ifPresent(c -> {
            c.setUsername(dto.getUsername());
            c.setPassword(dto.getPassword());
            c.setFullName(dto.getFullName());
            c.setAddress(dto.getAddress());
            c.setPhone(dto.getPhone());
            c.setEmail(dto.getEmail());
            c.setBirthDay(dto.getBirthDay());
            c.setActive(dto.isActive());
            repo.save(c);
        });
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
