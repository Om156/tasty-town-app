package com.tastytown.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tastytown.dto.CartItemRequestDTO;
import com.tastytown.dto.CartResponseDTO;
import com.tastytown.entity.Cart;
import com.tastytown.entity.CartItem;
import com.tastytown.entity.Food;
import com.tastytown.entity.UserEntity;
import com.tastytown.mapper.CartMapper;
import com.tastytown.repository.CartRepository;
import com.tastytown.repository.FoodRepository;
import com.tastytown.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    public CartResponseDTO addToCart(String userId, CartItemRequestDTO cartItemRequestDTO) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
            .orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setUser(user);
                return cartRepository.save(newCart);
            });

        Food food = foodRepository.findById(cartItemRequestDTO.getFoodId())
            .orElseThrow(() -> new RuntimeException("Food not found"));

        // Check if item already exists in cart
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
            .filter(item -> item.getFood().getFoodId().equals(food.getFoodId()))
            .findFirst();

        if (existingItemOpt.isPresent()) {
            // Update quantity if item exists
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + cartItemRequestDTO.getQuantity());
        } else {
            // Create new CartItem if not exists
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setFood(food);
            newItem.setQuantity(cartItemRequestDTO.getQuantity());
            cart.getItems().add(newItem);
        }

        cartRepository.save(cart);

        return CartMapper.convertToCartResponseDTO(cart);
    }


    public CartResponseDTO getCartByUserId(String userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    
        Cart cart = cartRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Cart not found"));
    
        return CartMapper.convertToCartResponseDTO(cart);
    }

    public CartResponseDTO updateItemQuantity(String userId, CartItemRequestDTO dto) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Find the item in the cart
        Optional<CartItem> itemOptional = cart.getItems().stream()
            .filter(item -> item.getFood().getFoodId().equals(dto.getFoodId()))
            .findFirst();

        if (itemOptional.isEmpty()) {
            throw new RuntimeException("Item not found in cart");
        }

        CartItem item = itemOptional.get();

        if (dto.getQuantity() <= 0) {
            // Remove item if quantity is 0 or negative
            cart.getItems().remove(item);
        } else {
            // Update the quantity
            item.setQuantity(dto.getQuantity());
        }

        // Save and return updated cart
        cartRepository.save(cart);
        return CartMapper.convertToCartResponseDTO(cart);
    }
    
    public CartResponseDTO removeItemFromCart(String userId, String foodId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    
        Cart cart = cartRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Cart not found"));
    
        List<CartItem> updatedItems = cart.getItems().stream()
            .filter(item -> !item.getFood().getFoodId().equals(foodId))
            .collect(Collectors.toList());
    
        cart.getItems().clear();
        cart.getItems().addAll(updatedItems);
    
        cartRepository.save(cart); // Persist changes
    
        return CartMapper.convertToCartResponseDTO(cart);
    }

    @Transactional // check if required then only
    public void clearCart(String userId) {
        cartRepository.deleteByUser_UserId(userId);
    }
}

