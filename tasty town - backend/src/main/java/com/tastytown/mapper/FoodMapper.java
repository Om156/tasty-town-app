package com.tastytown.mapper;

import java.io.File;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Value;

import com.tastytown.dto.FoodRequestDTO;
import com.tastytown.dto.FoodResponseDTO;
import com.tastytown.entity.Category;
import com.tastytown.entity.Food;

public class FoodMapper {
    public static Food convertToFood(FoodRequestDTO dto, Category category, String imageName) {
        Food food = new Food();
        food.setFoodName(dto.getFoodName());
        food.setFoodDescription(dto.getFoodDescription());
        food.setFoodPrice(dto.getFoodPrice());
        food.setFoodImage(imageName);
        food.setCategory(category);

        return food;
    }

    public static FoodResponseDTO convertToFoodResponseDTO(Food food) {
        // String baseUrl = "http://localhost:1200/images/";
        // String foodImagePath = baseUrl + food.getFoodImage();

        return new FoodResponseDTO(
                    food.getFoodId(),
                    food.getFoodName(), 
                    food.getFoodDescription(),
                    food.getFoodPrice(), 
                    // foodImagePath,
                    food.getFoodImage(),
                    food.getCategory().getCategoryId(),
                    food.getCategory().getCategoryName()
                );
    }


}
