package com.example.Products.model;

import java.util.Base64;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "products")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_ID")
    private Long id;
    
    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_weight")
    private float productWeight;

    @Column(name = "product_price")
    private float productPrice;

    @Column(name = "product_image")
    private byte[] productImage;

    @Column(name = "category_id")
    private int categoryId;

    public void setProductID(Long id){
        this.id = id;
    }
    public void setProductName(String productName){
        this.productName = productName;
    }
    public void setProductWeight(float productWeight){
        this.productWeight = productWeight;
    }
    public void setProductPrice(float productPrice){
        this.productPrice = productPrice;
    }
    public void setProductImage(byte[] product_image){
        this.productImage = product_image;
    }
    public void setCategoryId(int category_id){
        this.categoryId = category_id;
    }
    public Long getId(){
        return this.id;
    }
    public String getProductName(){
        return this.productName;
    }
    public float getProductWeight(){
        return this.productWeight;
    }
    public float getProductPrice(){
        return this.productPrice;
    }
    public String getProductImage(){
        return  Base64.getEncoder().encodeToString(this.productImage);
    }
    public int getCategoryId(){
        return this.categoryId;
    }
}
