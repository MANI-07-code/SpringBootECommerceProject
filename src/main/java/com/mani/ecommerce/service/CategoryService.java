package com.mani.ecommerce.service;

import com.mani.ecommerce.model.Category;
import com.mani.ecommerce.payload.CategoryDTO;
import com.mani.ecommerce.payload.CategoryResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
     CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
     CategoryDTO createCategory(CategoryDTO categoryDTO);
     CategoryDTO deleteCategory(Long categoryId);
     CategoryDTO updateCategory(CategoryDTO categoryDTO,Long categoryId);


}

