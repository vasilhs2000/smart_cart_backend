package com.example.Products.controller;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.Positive;


import com.example.Products.model.Products;
import com.example.Products.model.BasketConfirmation;
import com.example.Products.model.Baskets;
import com.example.Products.model.BasketsProducts;
import com.example.Products.model.Offers;
import com.example.Products.service.ProductService;


@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService proService;
    private Map<Long, Boolean> basketConfirmations = new HashMap<>();
    /*@RequestMapping(value="/exchange_keys/{key}", method=RequestMethod.GET)
    public String sendKey(@PathVariable (value="key")String key) throws NoSuchAlgorithmException {
        System.out.println("Public key of basket "+key);

        //calculate the shared key
        proService.exhangeKeys();
        return "";
        //byte[] publicKeyBytes = proService.exhangeKeys().getPublic().getEncoded();
        //BigInteger publicKetInt = new BigInteger(1,publicKeyBytes);
        //return publicKetInt.toString();
    }*/

    @RequestMapping(value="/products", method=RequestMethod.POST)
    public Products createProduct(@RequestBody Products pro) {
        return proService.createProduct(pro);
    }


    @RequestMapping(value="/products", method=RequestMethod.GET)
    public List<Products> readProducts() {
        return proService.getProducts();
    }

    @RequestMapping(value="/productsIDs", method=RequestMethod.GET)
    public List<Long> productsIDs() {
        return proService.getProductsIDs();
    }

    @RequestMapping(value="/basket/{basketID}", method=RequestMethod.GET)
    public List<Products> readProducts(@PathVariable(value = "basketID") @Positive Long basket_ID) {
        return proService.getBasketProducts(basket_ID);
         // Ελέγχουμε αν έχει επιβεβαιωθεί το καλάθι πριν προσθέσουμε το προϊόν
        /*if (basketConfirmations.getOrDefault(basket_ID, false)){ 
            proService.getBasketProducts(basket_ID);
            return ResponseEntity.ok("Product added to basket.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Basket confirmation required.");
        }*/
    }

    @RequestMapping(value="/productIds/{basketID}", method=RequestMethod.GET)
    public List<Long> getBasketProductIds(@PathVariable(value = "basketID") @Positive Long basket_ID) {
        return proService.getBasketProductIds(basket_ID);
    }

    @RequestMapping(value="basket/confirm/{basketID}/{basketConfirmation}", method=RequestMethod.PUT)
    public Baskets confirmBasket(@PathVariable(value = "basketID") Long basket_ID, @PathVariable(value = "basketConfirmation") int basket_Confirmation ) {
        return proService.confirmBasket(basket_ID , BasketConfirmation.fromValue(basket_Confirmation));
    }

    @RequestMapping(value="basketConfirmation/{basketID}", method=RequestMethod.GET)
    public int getBasketConfirmation(@PathVariable(value = "basketID") Long basket_ID ) {
        return proService.getBasketConfirmation(basket_ID);
    }

    @RequestMapping(value="basket/{basketID}/{productID}", method=RequestMethod.POST)
    public String addBasketProduct(@PathVariable(value = "basketID") @Positive Long basket_ID , @PathVariable(value = "productID") @Positive Long product_ID) {
        BasketsProducts newproduct = new BasketsProducts();
        newproduct.setBasketID(basket_ID);
        newproduct.setProductID(product_ID);
        return proService.addBasketProduct(newproduct);
    }

    @RequestMapping(value="basket/setTotalCost/{basketID}/{productID}/{plusMinus}", method=RequestMethod.PUT)
    public String setBasketCost(@PathVariable(value = "basketID") @Positive Long basket_ID, @Positive @PathVariable(value = "productID") Long product_ID, @PathVariable(value = "plusMinus") int plus_Minus ) {
        return proService.setBasketCost(basket_ID, product_ID, plus_Minus);
    }

    @RequestMapping(value="basket/{basketID}/{productID}", method=RequestMethod.DELETE)
    public String removeBasketProduct(@PathVariable(value = "basketID") @Positive Long basket_ID, @PathVariable(value = "productID") @Positive Long product_ID) {
        return proService.removeBasketProduct(product_ID);
    }

    @RequestMapping(value="/offers", method=RequestMethod.GET)
    public List<Offers> readOffers() {
        return proService.getOffers();
    }

   @RequestMapping(value="/offers/products", method=RequestMethod.GET)
    public List<Products> readProducts(@RequestParam("offerIds") List<Long> offerIds) {
        System.out.println(offerIds.size());
        return proService.getOfferProducts(offerIds);
    }

    //edw prepei na to allajw to link 
    @RequestMapping(value="/totalCost/{basketID}", method=RequestMethod.GET)
    public float getTotalCost(@PathVariable(value = "basketID") @Positive Long basket_ID) {
        return proService.getBasketCost(basket_ID);
    }

    @RequestMapping(value="/products/name/{productID}", method = RequestMethod.GET)
    public String getProductName(@PathVariable(value = "productID") @Positive Long id){
        return proService.getProductName(id);
    }
    
    @RequestMapping(value="/products/weight/{productID}", method = RequestMethod.GET)
    public float getProductWeight(@PathVariable(value = "productID") @Positive Long id){
        return proService.getProductWeight(id);
    }

    @RequestMapping(value="/products/price/{productID}", method = RequestMethod.GET)
    public float getProductPrice(@PathVariable(value = "productID") @Positive Long id){
        return proService.getProductPrice(id);
    }
    @RequestMapping(value="/products/image/{productID}", method = RequestMethod.GET)
    public String getProductImage(@PathVariable(value = "productID") @Positive Long id){
        return proService.getProductImage(id);
    }
    /*to idio kai edw 
    @RequestMapping(value="/products/{productID}/{basketID}", method=RequestMethod.PUT)
    public Product readProducts(@PathVariable(value = "productID") Long id, @PathVariable(value = "basketID") int basket_ID) {
        return proService.updateProduct(id, basket_ID);
    }
    */
    @RequestMapping(value="/products/{productID}", method=RequestMethod.DELETE)
    public void deleteProduct(@PathVariable(value = "productID") @Positive Long id) {
        proService.deleteProduct(id);
}
}
