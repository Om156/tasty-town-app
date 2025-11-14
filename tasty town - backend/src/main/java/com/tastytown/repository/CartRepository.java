package com.tastytown.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tastytown.entity.Cart;
import com.tastytown.entity.UserEntity;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUser(UserEntity user);
    void deleteByUser_UserId(String userId);
}
