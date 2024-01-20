package com.ecommerce.demo.ecommerce.backend.model.dao;

import com.ecommerce.demo.ecommerce.backend.model.LocalUser;
import com.ecommerce.demo.ecommerce.backend.model.WebOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebOrderDAO extends JpaRepository<WebOrder,Long> {

    List<WebOrder> findByUser(LocalUser user);
}
