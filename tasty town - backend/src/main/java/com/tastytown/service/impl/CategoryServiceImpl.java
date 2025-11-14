package com.tastytown.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tastytown.dto.CategoryRequestDTO;
import com.tastytown.entity.Category;
import com.tastytown.entity.Food;
import com.tastytown.exception.CategoryNotFoundException;
import com.tastytown.repository.CategoryRepository;
import com.tastytown.repository.FoodRepository;
import com.tastytown.service.CategoryService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final FoodRepository foodRepository;

    @Override
    public Category addCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category();
        category.setCategoryName(categoryRequestDTO.getCategoryName());
        return categoryRepository.save(category);
    }

    // @Override
    // public Category getCategoryById(String id) {
    //     Category category = categoryRepository.findById(id)
    //             .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));
    //     return category;
    // }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // @Override
    // public Category updateCategory(String id, Category updatedCategory) {
    //     Category existingCategory = categoryRepository.findById(id)
    //             .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));

    //     // Updating the entity fields from DTO
    //     existingCategory.setCategoryName(updatedCategory.getCategoryName());
    //     existingCategory.setCategoryDescription(updatedCategory.getCategoryDescription());

    //     categoryRepository.save(existingCategory);
    //     return existingCategory;
    // }


    @Override
    @Transactional
    public void deleteCategory(String id) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));
        
        // first normal delete kariki dekhao ki delete hebani becasue of foreign key constraint 
        //so ame kana kariba basically first tara associated foods ku dlete kariba
        // ta gote method create kara food repository re ! 
        foodRepository.deleteByCategory(existingCategory);
        // still gote exception transation pai so add kara @transaction
        categoryRepository.delete(existingCategory);
    }

    @Override
    public List<Food> getFoodsByCategoryId(String categoryId) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        return foodRepository.findByCategory_CategoryId(categoryId);
    }   
}
