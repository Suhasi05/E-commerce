package com.example.E_commerce.Services;

import com.example.E_commerce.Model.Category;
import com.example.E_commerce.Repository.categoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    categoryRepository categoryRepository;

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).get();
    }

    public void deleteCategoryById(int id) {
        categoryRepository.deleteById(id);
    }

    public Optional<Category> getCategoryBtId(int id) {
        return categoryRepository.findById(id);
    }

    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }
}
