package com.example.Products.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Products.model.Products;
import com.example.Products.model.BasketConfirmation;
import com.example.Products.model.Baskets;
import com.example.Products.model.BasketsProducts;
import com.example.Products.model.Offers;
import com.example.Products.repository.BasketsProductsRepository;
import com.example.Products.repository.BasketsRepository;
import com.example.Products.repository.OffersRepository;
import com.example.Products.repository.ProductRepository;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

@Service
public class ProductService {
    @Autowired
        private ProductRepository proRepository; 
    @Autowired
        private OffersRepository offRepository;
    @Autowired
        private BasketsProductsRepository baskProductsRepository;
    @Autowired
        private BasketsRepository basketsRepository;
   

    //excange keys with end point
    public SecretKey exhangeKeys() throws NoSuchAlgorithmException{
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey key = keyGenerator.generateKey();
        System.out.println("to key einai "+key);
        return key;

        /* 
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DiffieHellman");
		keyPairGen.initialize(512);
		KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;*/
    }

    //create
    public Products createProduct(Products pro){
        return proRepository.save(pro);
    }

    //read_all
    public List<Products> getProducts(){
        return proRepository.findAll();
    }

    public List<Long> getProductsIDs(){
        List<Long> IDs = new ArrayList<Long>();
        for(int i=0;i<proRepository.findAll().size();i++)
            IDs.add(proRepository.findAll().get(i).getId());
        return IDs;
    }

    //list of products in basket
    public List<Products> getBasketProducts(Long basket_ID){
        List<Products> basketList = new ArrayList<Products>();

        for(int i=0; i<baskProductsRepository.findByBasketId(basket_ID).size();i++)
            basketList.add(proRepository.findById(baskProductsRepository.findByBasketId(basket_ID).get(i).getProductId()).orElse(null));
        return basketList;
    }

    //lista me ta ids pou yparxoun sto kalathi
    public List<Long> getBasketProductIds(Long basket_ID){
        List<Long> basketProductIds = new ArrayList<Long>();
        for(int i=0; i<baskProductsRepository.findByBasketId(basket_ID).size();i++)
            basketProductIds.add(baskProductsRepository.findByBasketId(basket_ID).get(i).getProductId());
        return basketProductIds;
    }

    //confirm basket
    public Baskets confirmBasket(Long basket_ID, BasketConfirmation basket_Confirmation){
        Baskets basket = basketsRepository.findById(basket_ID).get();
        basket.setBasketConfirmation(basket_Confirmation);
        return basketsRepository.save(basket);
    }

    //mono an to basket_Confirmation einai 1 mporei na sundethei otidhpote allo den mporei 
    public int getBasketConfirmation(Long basket_ID){
        int basketConfirmation =0;
        for(int i=0;i<basketsRepository.findAll().size();i++)
            if(basketsRepository.findAll().get(i).getBasketId().equals(basket_ID))
                basketConfirmation = basketsRepository.findAll().get(i).getBasketConfirmation().getValue();
        
        if(basketConfirmation==1)
            return basketConfirmation;
        else
            return 0;
        
    }

    public String setBasketCost( Long basket_ID , Long product_ID , int plus_Minus){
        Baskets basket = basketsRepository.findById(basket_ID).get();
        if(plus_Minus==0)
            basket.setBasketCost(basket.getBasketCost()+proRepository.findById(product_ID).get().getProductPrice());
        else if(plus_Minus==1)
            basket.setBasketCost(basket.getBasketCost()-proRepository.findById(product_ID).get().getProductPrice());
        basketsRepository.save(basket);
        return basket.getBasketCost()+" $";
    }

    //eisagwgh proiontos
    public String addBasketProduct( BasketsProducts newProduct ){
        baskProductsRepository.save(newProduct);
        Products product = proRepository.findById(newProduct.getProductId()).get();
        return product.getProductName()+". "+product.getProductPrice()+" $";
    }
    
    //afairesh proiontos
    public String removeBasketProduct( Long product_ID){
        Products product = proRepository.findById(product_ID).get();
        Long ID = 0L;
        for(int i=0;i<baskProductsRepository.findAll().size();i++)
            if(baskProductsRepository.findAll().get(i).getProductId().equals(product_ID))
                ID = baskProductsRepository.findAll().get(i).getID();
        
        baskProductsRepository.deleteById(ID);
        return product.getProductName()+". "+product.getProductPrice()+" $";
    }

    //lista me ta offers pou uparxoun
    public List<Offers> getOffers(){
        return offRepository.findAll();
    }

    //lista apo ta proionta pou exoun offer
    public List<Products> getOfferProducts(List<Long> offerIds){
        List<Products> offerProducts = new ArrayList<Products>() ;
        for(int i=0; i<offerIds.size();i++)
            offerProducts.add(proRepository.findById(offerIds.get(i)).orElse(null));
            
        return offerProducts;
    }

    //total cost of basket
    public float getBasketCost(Long basket_ID){
        return basketsRepository.findById(basket_ID).get().getBasketCost();
    }
    /*
    Auth edw einai idia me thn getBasketCost. Apla se periptwsh pou den brei to basket_ID tha bgalei kostos 0.
    Apla den jerw an xreiazetai na to blaw giati se otan den to briskei to ID apla den sundeetai h efarmogh.

    public float getBasketCost(Long basket_ID) {
        indById(basket_ID)
        .map(Basket::getBasketCost)
        .orElse(0.0F); // Επιστροφή μηδενικού κόστους αν το καλάθι δεν βρεθεί
    }
    */
    //read_by_id_return_name
    public String getProductName(Long proID){
        return proRepository.findById(proID).get().getProductName();
    }

    //read_by_id_return_weight
    public float getProductWeight(Long proID){
        return proRepository.findById(proID).get().getProductWeight();
    }

    //read_by_id_return_price
    public float getProductPrice(Long proID){
        return proRepository.findById(proID).get().getProductPrice();
    }

    //read_by_id_return_image
    public String getProductImage(Long proID){
        return proRepository.findById(proID).get().getProductImage();
    }

    //delete
    public void deleteProduct(Long proID){
        proRepository.deleteById(proID);
    }
    /*edw prepei na kanw thn allagh gia n ftiajw th sunarthsh sto raspberry 
    //update
    public Product updateProduct(Long proID , int basket_ID){
        Product pro = proRepository.findById(proID).get();
        pro.setProductBasket_ID(basket_ID);

        return proRepository.save(pro);
    }*/
}
