package com.example.E_commerce.Controller;

import com.example.E_commerce.Model.Category;
import com.example.E_commerce.Model.Product;
import com.example.E_commerce.Services.CategoryService;
import com.example.E_commerce.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        return "index";
    }

    @GetMapping("/shop")
    public String shop(Model model) {
        List<Product> products = productService.getAllProducts();
        List<Category > categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "shop";
    }

    @GetMapping("/shop/category/{id}")
    public String category(Model model, @PathVariable int id) {
        List<Category> categories = categoryService.getAllCategories();
        List<Product> products = productService.getProductsByCategory(id);
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        model.addAttribute("id", id);
        return "shop";
    }

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(Model model, @PathVariable Long id) {
        model.addAttribute("product", productService.getProductById(id).get());
        return "viewProduct";
    }

}
