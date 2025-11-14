package com.tastytown.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tastytown.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String>{
    Optional<UserEntity> findByUserEmail(String email);
}
