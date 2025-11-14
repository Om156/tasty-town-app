package com.tastytown.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tastytown.dto.CartItemRequestDTO;
import com.tastytown.dto.CartResponseDTO;
import com.tastytown.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addToCart(
            @RequestAttribute String userId,
            @RequestBody CartItemRequestDTO cartItemRequestDTO) {

        return ResponseEntity.ok(cartService.addToCart(userId, cartItemRequestDTO));
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCartByUserId(@RequestAttribute String userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PutMapping("/items")
    public ResponseEntity<CartResponseDTO> updateItemQuantity(
            @RequestAttribute String userId,
            @RequestBody CartItemRequestDTO cartItemRequestDTO) {
        
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, cartItemRequestDTO));
    }

    @DeleteMapping("/items/{foodId}")
    public ResponseEntity<CartResponseDTO> removeItem(
            @RequestAttribute String userId,
            @PathVariable String foodId) {
        return ResponseEntity.ok(cartService.removeItemFromCart(userId, foodId));
    }
    

    // clear cartItems
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@RequestAttribute String userId) {
        cartService.clearCart(userId);
    }
}
