package com.example.Products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Products.model.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long>  {
    
}

