package com.tastytown.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tastytown.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
}

