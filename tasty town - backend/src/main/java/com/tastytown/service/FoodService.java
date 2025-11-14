package com.tastytown.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tastytown.dto.FoodRequestDTO;
import com.tastytown.dto.FoodResponseDTO;
import com.tastytown.entity.Category;
import com.tastytown.entity.Food;
import com.tastytown.mapper.FoodMapper;
import com.tastytown.repository.CategoryRepository;
import com.tastytown.repository.FoodRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodService {

private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;

    @Value("${upload.file.dir}")
    private String FILE_DIR;

    // public FoodResponseDTO createFood(FoodRequestDTO foodDTO) {

    //     Category category = categoryRepository.findById(foodDTO.getCategoryId())
    //             .orElseThrow(() -> new RuntimeException("Category not found"));

    //     // Map DTO to entity
    //     Food food = FoodMapper.convertToFood(foodDTO, category);
    //     food = foodRepository.save(food);
    //     return FoodMapper.convertToFoodResponseDTO(food);
    // }

    public FoodResponseDTO createFood(FoodRequestDTO foodDTO, MultipartFile foodImage) throws IOException{

        Category category = categoryRepository.findById(foodDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        String imageName = uploadFile(foodImage);
        // Map DTO to entity

        Food food = FoodMapper.convertToFood(foodDTO, category, imageName);
        food = foodRepository.save(food);
        return FoodMapper.convertToFoodResponseDTO(food);
    }
    
    public FoodResponseDTO getFoodById(String id) {
        Food existingFood = foodRepository.findById(id).orElseThrow(() -> new RuntimeException("Fodd Not Found with id " + id));
        return FoodMapper.convertToFoodResponseDTO(existingFood);
    }
    
    public List<FoodResponseDTO> getAllFoods() {
        List<Food> foods = foodRepository.findAll();
        // return foods.stream().map(food -> FoodMapper.convertToFoodResponseDTO(food)).toList();
        return foods.stream().map(FoodMapper :: convertToFoodResponseDTO).toList();
    }

    // later added
    // public Page<FoodResponseDTO> getPaginatedFoods(int page, int size) {
    //     Pageable pageable = PageRequest.of(page, size);
    //     Page<Food> foodPage = foodRepository.findAll(pageable);
        
    //     return foodPage.map(food -> new FoodResponseDTO(
    //         food.getFoodId(),
    //         food.getFoodName(),
    //         food.getFoodDescription(),
    //         food.getFoodPrice(),
    //         food.getCategory().getCategoryId(),
    //         food.getCategory().getCategoryName()
    //     ));
    // }

    // more later
    public Page<FoodResponseDTO> getFilteredPaginatedFoods(int page, int size, String categoryId, String search) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Food> foodPage;

        if (categoryId != null && search != null && !categoryId.equals("all") && !search.isBlank()) {
            foodPage = foodRepository.findByCategory_CategoryIdAndFoodNameContainingIgnoreCase(categoryId, search, pageable);
        } else if (categoryId != null && !categoryId.equals("all")) {
            foodPage = foodRepository.findByCategory_CategoryId(categoryId, pageable);
        } else if (search != null && !search.isBlank()) {
            foodPage = foodRepository.findByFoodNameContainingIgnoreCase(search, pageable);
        } else {
            foodPage = foodRepository.findAll(pageable);
        }

        // return foodPage.map(food -> new FoodResponseDTO(
        //     food.getFoodId(),
        //     food.getFoodName(),
        //     food.getFoodDescription(),
        //     food.getFoodPrice(),
        //     food.getCategory().getCategoryId(),
        //     food.getCategory().getCategoryName()
        // ));

        return foodPage.map(FoodMapper :: convertToFoodResponseDTO);
    }
    
    public Food updateFood(String id, FoodRequestDTO foodDTO) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found"));

        Category category = categoryRepository.findById(foodDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        food.setFoodName(foodDTO.getFoodName());
        food.setFoodPrice(foodDTO.getFoodPrice());
        food.setCategory(category);

        return foodRepository.save(food);
    
    }
    
    public void deleteFood(String id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food not found"));
        foodRepository.delete(food);
    }

    private String uploadFile(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            var fileName = file.getOriginalFilename();
            var newFileName = generateFileName(fileName);

            var fos = new FileOutputStream(FILE_DIR + File.separator + newFileName);
            fos.write(file.getBytes());
            fos.close();
            return newFileName;
        }

        throw new FileNotFoundException("File Not Found");
    }

    private String generateFileName(String fileName) {
        var extensionName = fileName.substring(fileName.lastIndexOf("."));
        var newFileName = UUID.randomUUID().toString();
        return newFileName + extensionName;
    }
}
