package com.example.Products.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity 
@Table(name = "baskets")
public class Baskets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_ID")
    private Long basketId;
    
    @Column(name = "cost")
    private float basketCost;

    @Column(name = "confirmation")
    private BasketConfirmation basketConfirmation;

    public void setBasketID(Long basketId){
        this.basketId = basketId;
    }
    public void setBasketCost(float basketCost){
        this.basketCost = basketCost;
    }
    public void setBasketConfirmation(BasketConfirmation basketConfirmation){
        this.basketConfirmation = basketConfirmation;
    }
    public Long getBasketId(){
        return this.basketId;
    }
    public float getBasketCost(){
        return this.basketCost;
    }
    public BasketConfirmation getBasketConfirmation(){
        return this.basketConfirmation;
    }
}
