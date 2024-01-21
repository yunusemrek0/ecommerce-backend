package com.ecommerce.demo.ecommerce.backend.model.dao;

import com.ecommerce.demo.ecommerce.backend.model.LocalUser;
import com.ecommerce.demo.ecommerce.backend.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenDAO extends JpaRepository<VerificationToken,Long> {

    Optional<VerificationToken> findByToken(String token);

    void deleteByUser(LocalUser user);
}
