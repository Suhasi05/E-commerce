package com.example.E_commerce.Controller;

import com.example.E_commerce.Model.Category;
import com.example.E_commerce.Model.Product;
import com.example.E_commerce.Services.CategoryService;
import com.example.E_commerce.Services.ProductService;
import com.example.E_commerce.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AdminController {
    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

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
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String updateCategory(@PathVariable Long id, Model model) {
        Optional<Category> category= categoryService.getCategoryById(id);
        if(category.isPresent()) {
            model.addAttribute("category", category.get());
            return "CategoriesAdd";
        } else {
            return "404";
        }

    }

//    Product Section

    @GetMapping("/admin/products")
    public String products(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "Products";
    }

    @GetMapping("/admin/products/add")
    public String addProduct(Model model) {
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "ProductsAdd";
    }

    @PostMapping("/admin/products/add")
    public String addProduct(@ModelAttribute("productDTO") ProductDTO productDTO,
                             @RequestParam("productImage") MultipartFile file,
                             @RequestParam("imgName") String imgName) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found")));
        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());

        String imageUUID;
        try {
            // Ensure the upload directory exists
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            if (!file.isEmpty()) {
                // Validate file type and size
                if (!file.getContentType().startsWith("image/")) {
                    throw new IllegalArgumentException("Invalid file type. Please upload an image.");
                }
                if (file.getSize() > 5 * 1024 * 1024) { // 5MB size limit
                    throw new IllegalArgumentException("File size exceeds the maximum limit of 5MB.");
                }

                // Generate a unique file name
                imageUUID = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path fileNameAndPath = uploadPath.resolve(imageUUID);
                Files.write(fileNameAndPath, file.getBytes());
            } else {
                imageUUID = imgName; // Use default image name if no file is uploaded
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }

        product.setImageName(imageUUID);
        productService.addProduct(product);

        return "redirect:/admin/products";
    }


    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id).get();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setWeight(product.getWeight());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setImageName(product.getImageName());

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("productDTO", productDTO);
        return "ProductsAdd";
    }

}
