package com.mani.ecommerce.service;

import com.mani.ecommerce.Exceptions.APIException;
import com.mani.ecommerce.Exceptions.ResourceNotFoundException;
import com.mani.ecommerce.Repository.CategoryRepository;
import com.mani.ecommerce.model.Category;
import com.mani.ecommerce.payload.CategoryDTO;
import com.mani.ecommerce.payload.CategoryResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;


@Service
public class CategoryServiceImple implements CategoryService{
//    private List<Category> categories = new ArrayList<>();

    private CategoryRepository categoryRepository;
    CategoryServiceImple(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber , Integer pageSize,String sortBy,String sortOrder){
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();
        if(categories.isEmpty()){
            throw new APIException("No category created till now.");
        }
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalpages(categoryPage.getTotalPages());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

     @Override
     public CategoryDTO createCategory(CategoryDTO categoryDTO){
        Category category = modelMapper.map(categoryDTO,Category.class);
        Category savedCategoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategoryFromDB !=null){
            throw new APIException("Category with the name "+ category.getCategoryName()+"already exists!!");
        }
     Category savedCategory = categoryRepository.save(category);

        return modelMapper.map(savedCategory, CategoryDTO.class);


     }




    @Override
    public CategoryDTO deleteCategory(Long categoryId){

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category","categoryId",categoryId));

        CategoryDTO categoryDTO = modelMapper.map(category,CategoryDTO.class);

        categoryRepository.delete(category);
//         Category category = categories.stream()
//                 .filter(c -> c.getCategoryId().equals(categoryId))
//                 .findFirst().orElseThrow(() -> new ResourceNotFoundException("category","categoryId",categoryId));
//
//         categories.remove(category);
         return categoryDTO;
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO,Long categoryId) {
        Category category = modelMapper.map(categoryDTO,Category.class);

        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category","categoryId",categoryId));
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);


    }
}
