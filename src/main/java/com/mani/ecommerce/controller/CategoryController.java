package com.mani.ecommerce.controller;


import com.mani.ecommerce.model.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {

    private List<Category> categories = new ArrayList<>();

    @GetMapping("/api/public/categories")
    private List<Category> getAllCategories(){
        return categories;
    }


}
