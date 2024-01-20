package com.ecommerce.demo.ecommerce.backend.model.dao;

import com.ecommerce.demo.ecommerce.backend.model.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface LocalUserDAO extends JpaRepository<LocalUser,Long> {

    Optional<LocalUser> findByUsername(String username);

    Optional<LocalUser> findByEmail(String email);
}
