package com.tastytown.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class FoodResponseDTO {
    private String foodId;
    private String foodName;
    private String foodDescription;
    private double foodPrice;
    private String foodImage;
    private String categoryId;
    private String categoryName;
}

