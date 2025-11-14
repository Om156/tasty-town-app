package com.tastytown.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodRequestDTO {
    private String foodName;
    private String foodDescription;
    private double foodPrice;
    private String categoryId;
}

