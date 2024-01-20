package com.ecommerce.demo.ecommerce.backend.api.controller.product;

import com.ecommerce.demo.ecommerce.backend.model.Product;
import com.ecommerce.demo.ecommerce.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductContorller {

    @Autowired
    private ProductService productService;


    @GetMapping
    public List<Product> getProducts(){
        return productService.getProducts();
    }
}
