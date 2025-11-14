package com.tastytown.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tastytown.entity.OrderEntity;
import com.tastytown.entity.UserEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findByUser(UserEntity user);
}
