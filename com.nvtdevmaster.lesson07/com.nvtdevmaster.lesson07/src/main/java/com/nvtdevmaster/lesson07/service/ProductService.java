package com.nvtdevmaster.lesson07.service;

import com.nvtdevmaster.lesson07.entity.Product;
import com.nvtdevmaster.lesson07.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // READ ALL
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // READ BY ID
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // CREATE / UPDATE
    public Product save(Product product) {
        return productRepository.save(product);
    }

    // DELETE
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}
