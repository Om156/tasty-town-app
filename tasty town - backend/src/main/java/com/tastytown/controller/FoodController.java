package com.tastytown.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tastytown.dto.FoodRequestDTO;
import com.tastytown.dto.FoodResponseDTO;   
import com.tastytown.service.FoodService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    @Autowired
    private FoodService foodService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FoodResponseDTO> createFood(@RequestPart("foodData") String rawJson, @RequestPart MultipartFile foodImage) throws IOException{
        // return ResponseEntity.ok(foodService.createFood(foodDTO, foodImage));
            ObjectMapper objectMapper = new ObjectMapper();
        FoodRequestDTO dto = objectMapper.readValue(rawJson, FoodRequestDTO.class);
        return ResponseEntity.ok(foodService.createFood(dto, foodImage));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodResponseDTO> getFoodById(@PathVariable String id) {
        return ResponseEntity.ok(foodService.getFoodById(id));
    }

    @GetMapping
    public ResponseEntity<List<FoodResponseDTO>> getAllFoods() {
        return ResponseEntity.ok(foodService.getAllFoods());
    }

    // later added
    // @GetMapping("/paginated-foods")
    // public ResponseEntity<Page<FoodResponseDTO>> getPaginatedFoods(
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "10") int size) {
    //     return ResponseEntity.ok(foodService.getPaginatedFoods(page, size));
    // }

    // more later
    @GetMapping("/paginated-foods")
    public ResponseEntity<Page<FoodResponseDTO>> getPaginatedFoods(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String search) {
        
        return ResponseEntity.ok(foodService.getFilteredPaginatedFoods(page, size, categoryId, search));
    }


    // @PutMapping("/{id}")
    // public ResponseEntity<Food> updateFood(@PathVariable String id, @RequestBody FoodDTO foodDTO) {
    //     return ResponseEntity.ok(foodService.updateFood(id, foodDTO));
    // }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable String id) {
        foodService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }
}
