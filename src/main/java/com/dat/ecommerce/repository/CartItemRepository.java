package com.dat.ecommerce.repository;

import com.dat.ecommerce.entity.Cart;
import com.dat.ecommerce.entity.CartItem;
import com.dat.ecommerce.entity.Product;
import com.dat.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCart(Cart cart);
    Optional<CartItem> findByCartAndProduct(User user, Product product);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    void deleteByCart(Cart cart);
}
