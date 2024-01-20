package com.ecommerce.demo.ecommerce.backend.service;

import com.ecommerce.demo.ecommerce.backend.model.Product;
import com.ecommerce.demo.ecommerce.backend.model.dao.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {


    @Autowired
    private ProductDAO productDAO;


    public List<Product> getProducts(){
        return productDAO.findAll();
    }
}
