package com.tastytown.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tastytown.entity.Category;
import com.tastytown.entity.Food;

@Repository
public interface FoodRepository extends JpaRepository<Food, String> {
    void deleteByCategory(Category category);

    List<Food> findByCategory_CategoryId(String categoryId);

    // later added 
    Page<Food> findAll(Pageable pageable);

    // again later
    Page<Food> findByFoodNameContainingIgnoreCase(String foodName, Pageable pageable);

    Page<Food> findByCategory_CategoryId(String categoryId, Pageable pageable);

    Page<Food> findByCategory_CategoryIdAndFoodNameContainingIgnoreCase(String categoryId, String foodName, Pageable pageable);

}

