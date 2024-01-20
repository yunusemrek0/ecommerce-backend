package com.ecommerce.demo.ecommerce.backend.model.dao;


import com.ecommerce.demo.ecommerce.backend.model.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDAO extends JpaRepository<Product,Long> {


}
