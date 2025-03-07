package com.example.Products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Products.model.Baskets;

@Repository
public interface BasketsRepository extends JpaRepository<Baskets, Long>  {
    
}

