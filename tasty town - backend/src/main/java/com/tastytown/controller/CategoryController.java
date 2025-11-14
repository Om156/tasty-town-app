package com.tastytown.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tastytown.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import com.tastytown.dto.CategoryRequestDTO;
import com.tastytown.entity.Category;
import com.tastytown.entity.Food;;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category API",
        description = "This controller class manages CRUD operation for category entities")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Create a new category")
    // @ApiResponse(responseCode = "201", description = "category created")
    // @ApiResponses(value = {
    //     @ApiResponse(responseCode = "201", description = "Category created",
    //                 content = @Content(mediaType = "application/json",
    //                 schema = @Schema(implementation = Category.class))),
    //     @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    // })
    public ResponseEntity<Category> addCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        return new ResponseEntity<>(categoryService.addCategory(categoryRequestDTO), HttpStatus.CREATED);
    }
    
    // @GetMapping("/{id}")
    // public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
    //     return ResponseEntity.ok(categoryService.getCategoryById(id));
    // }
    
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
    
    // @PutMapping("/{id}")
    // public ResponseEntity<Category> updateCategory(@PathVariable String id, @RequestBody Category category) {
    //     return ResponseEntity.ok(categoryService.updateCategory(id, category));
    // }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // added new controller 
    @GetMapping("/{categoryId}/foods")
    public ResponseEntity<List<Food>> getFoodsByCategory(@PathVariable String categoryId) {
        List<Food> foods = categoryService.getFoodsByCategoryId(categoryId);
        return ResponseEntity.ok(foods);
    }

}
