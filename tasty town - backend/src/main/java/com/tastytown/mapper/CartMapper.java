package com.tastytown.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.tastytown.dto.CartItemResponseDTO;
import com.tastytown.dto.CartResponseDTO;
import com.tastytown.entity.Cart;

public class CartMapper {
    private CartMapper() {
    }

    public static CartResponseDTO convertToCartResponseDTO(Cart cart) {
        List<CartItemResponseDTO> items = cart.getItems().stream()
                .map(item -> new CartItemResponseDTO(item.getFood().getFoodId(),
                        item.getQuantity(),
                        item.getFood().getFoodName(), item.getFood().getCategory().getCategoryName(),
                        item.getFood().getFoodPrice()))
                .collect(Collectors.toList());

        return new CartResponseDTO(items);
    }
}
