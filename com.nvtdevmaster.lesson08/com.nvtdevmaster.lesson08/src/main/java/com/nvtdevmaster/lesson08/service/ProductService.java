package com.nvtdevmaster.lesson08.service;

import com.nvtdevmaster.lesson08.entity.Product;
import com.nvtdevmaster.lesson08.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> findAll() { return repo.findAll(); }

    public Product save(Product p) { return repo.save(p); }

    public Product findById(Long id) { return repo.findById(id).orElse(null); }

    public void delete(Long id) { repo.deleteById(id); }
}
