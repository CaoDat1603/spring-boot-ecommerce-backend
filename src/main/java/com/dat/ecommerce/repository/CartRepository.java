package com.dat.ecommerce.repository;

import com.dat.ecommerce.entity.Cart;
import com.dat.ecommerce.entity.User;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
