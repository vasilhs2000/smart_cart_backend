package com.example.Products.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "offers")
public class Offers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_ID")
    private Long id;
    
    @Column(name = "discount")
    private String offerDiscount;

    @Column(name = "price")
    private float offerPrice;

    public void setProductID(Long id){
        this.id = id;
    }
    public void setOfferDiscount(String offerDiscount){
        this.offerDiscount = offerDiscount;
    }
    public void setOfferPrice(float offerPrice){
        this.offerPrice = offerPrice;
    }
    public Long getId(){
        return this.id;
    }
    public String getOfferDiscount(){
        return this.offerDiscount;
    }
    public float getOfferPrice(){
        return this.offerPrice;
    }
}
