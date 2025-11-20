package com.nvtdevmaster.lesson08.service;

import com.nvtdevmaster.lesson08.entity.ProductConfig;
import com.nvtdevmaster.lesson08.repository.ProductConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductConfigService {

    @Autowired
    private ProductConfigRepository productConfigRepository;

    // Lưu 1 cấu hình của sản phẩm (RAM, CPU...)
    public ProductConfig save(ProductConfig productConfig) {
        return productConfigRepository.save(productConfig);
    }

    // Xóa cấu hình thuộc 1 sản phẩm
    public void delete(Long id) {
        productConfigRepository.deleteById(id);
    }
}
