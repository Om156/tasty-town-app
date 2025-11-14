package com.tastytown.service;

import java.util.List;

import com.tastytown.dto.CategoryRequestDTO;
import com.tastytown.entity.Category;
import com.tastytown.entity.Food;

public interface CategoryService {
    Category addCategory(CategoryRequestDTO categoryRequestDTO);

    // Category getCategoryById(String id);

    List<Category> getAllCategories();

    // Category updateCategory(String id, Category category);

    List<Food> getFoodsByCategoryId(String id);

    void deleteCategory(String id);
}
