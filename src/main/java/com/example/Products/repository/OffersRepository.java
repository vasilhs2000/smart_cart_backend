package com.example.Products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Products.model.Offers;

@Repository
public interface OffersRepository extends JpaRepository<Offers, Long>  {
    
}

