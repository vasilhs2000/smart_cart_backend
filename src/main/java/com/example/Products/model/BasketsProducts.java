package com.example.Products.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "baskets_products")
public class BasketsProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long ID;

    @Column(name = "product_ID")
    private Long productId;
    
    @Column(name = "basket_ID")
    private Long basketId;

    public void setProductID(Long productId){
        this.productId = productId;
    }
    public void setBasketID(Long basketId){
        this.basketId = basketId;
    }
    public Long getID(){
        return this.ID;
    }
    public Long getProductId(){
        return this.productId;
    }
    public Long getBasketId(){
        return this.basketId;
    }
}
