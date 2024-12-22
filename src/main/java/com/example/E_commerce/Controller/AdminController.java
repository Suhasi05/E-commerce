package com.example.E_commerce.Controller;

import com.example.E_commerce.Model.Category;
import com.example.E_commerce.Services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/admin")
    public String admin() {
        return "AdminHome";
    }

    @GetMapping("/admin/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "Categories";
    }

    @GetMapping("/admin/categories/add")
    public String addCategory(Model category) {
        category.addAttribute("category", new Category());
        return "CategoriesAdd";
    }

    @PostMapping("/admin/categories/add")
    public String addCategory(@ModelAttribute("category") Category category) {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String updateCategory(@PathVariable int id, Model model) {
        Optional<Category> category= categoryService.getCategoryBtId(id);
        if(category.isPresent()) {
            model.addAttribute("category", category.get());
            return "CategoriesAdd";
        } else {
            return "404";
        }

    }

    @GetMapping("/admin/products")
    public String products() {
        return "Products";
    }

    @GetMapping("/admin/products/add")
    public String addProduct() {
        return "ProductsAdd";
    }
}
