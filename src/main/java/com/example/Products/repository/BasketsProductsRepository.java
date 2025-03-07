package com.example.Products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Products.model.BasketsProducts;
import java.util.List;


@Repository
public interface BasketsProductsRepository extends JpaRepository<BasketsProducts, Long>  {
    List<BasketsProducts> findByBasketId(Long basketId);
}

