package com.tastytown.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private String foodId;
    private int quantity;

    private String foodName;
    private String foodCategory;
    private double foodPrice;
}
