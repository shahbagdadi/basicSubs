package com.netflix.subscription.controller;

import com.netflix.subscription.entity.Product;
import com.netflix.subscription.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/subscriptions")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;


    @PostMapping("/products")
    public Product create(@RequestBody Product product)
    {
        return productRepository.save(product);
    }


    @GetMapping("/products")
    public List<Product> findAll()
    {
        return productRepository.findAll();
    }


    @PutMapping("/products/{product_id}")
    public Product update(@PathVariable("product_id") Long productId, @RequestBody Product productObject)
    {
        Product product = productRepository.findOne(productId);
        product.setSku(productObject.getSku());
        product.setStatus(productObject.getStatus());
        product.setEntitlementSetId(productObject.getEntitlementSetId());
        return productRepository.save(product);
    }



    @DeleteMapping("/products/{product_id}")
    public List<Product> delete(@PathVariable("product_id") Long productId)
    {
        productRepository.delete(productId);
        return productRepository.findAll();
    }



    @GetMapping("/products/{product_id}")
    @ResponseBody
    public Product findByProductId(@PathVariable("product_id") Long productId)
    {
        return productRepository.findOne(productId);
    }
}
