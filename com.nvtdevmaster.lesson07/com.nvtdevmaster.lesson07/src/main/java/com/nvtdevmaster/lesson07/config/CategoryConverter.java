package com.nvtdevmaster.lesson07.config;

import com.nvtdevmaster.lesson07.entity.Category;
import com.nvtdevmaster.lesson07.repository.CategoryRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter implements Converter<String, Category> {

    private final CategoryRepository categoryRepository;

    public CategoryConverter(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category convert(String id) {
        return categoryRepository.findById(Long.valueOf(id))
                .orElse(null);
    }
}
