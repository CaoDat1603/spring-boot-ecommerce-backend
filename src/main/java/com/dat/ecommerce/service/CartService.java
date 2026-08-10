package com.dat.ecommerce.service;

import com.dat.ecommerce.dto.request.AddCartItemRequest;
import com.dat.ecommerce.dto.response.CartItemResponse;
import com.dat.ecommerce.dto.response.CartResponse;
import com.dat.ecommerce.entity.Cart;
import com.dat.ecommerce.entity.CartItem;
import com.dat.ecommerce.entity.Product;
import com.dat.ecommerce.entity.User;
import com.dat.ecommerce.exception.ProductNotFoundException;
import com.dat.ecommerce.exception.UserNotFoundException;
import com.dat.ecommerce.repository.CartItemRepository;
import com.dat.ecommerce.repository.CartRepository;
import com.dat.ecommerce.repository.ProductRepository;
import com.dat.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public CartResponse createCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Cart cart = new Cart(user);

        cart = cartRepository.save(cart);

        return toCartResponse(cart);
    }

    @Transactional
    public CartResponse getCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        return toCartResponse(cart);
    }

    public CartResponse addItem(
            Long userId,
            AddCartItemRequest request
    ) {
        Cart cart = getOrCreateCart(userId);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + request.getProductId()));

        if (product.getStock() == null ||
                product.getStock() < request.getQuantity()) {

            throw new RuntimeException(
                    "Not enough stock"
            );
        }

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(
                cart,
                product
        ).orElse(null);

        if (cartItem != null) {
            int newQuantity =
                    cartItem.getQuantity()
                            + request.getQuantity();

            if (newQuantity > product.getStock()) {
                throw new RuntimeException(
                        "Not enough stock"
                );
            }

            cartItem.setQuantity(newQuantity);
            cartItem.setUpdatedAt(
                    LocalDateTime.now()
            );

        } else {
            cartItem = new CartItem(
                    cart,
                    product,
                    product.getPrice(),
                    request.getQuantity()
            );

            cartItemRepository.save(cartItem);
        }

        cart.setUpdatedAt(LocalDateTime.now());

        return toCartResponse(cart);
    }

    public CartResponse updateItemQuantity(
            Long userId,
            Long cartItemId,
            Integer quantity
    ) {
        if (quantity == null || quantity < 1) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        CartItem cartItem =
                cartItemRepository.findById(cartItemId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cart item not found"
                                )
                        );

        // SECURITY CHECK
        if (!cartItem.getCart()
                .getId()
                .equals(cart.getId())) {

            throw new RuntimeException(
                    "Cart item does not belong to this cart"
            );
        }

        Product product = cartItem.getProduct();

        if (product.getStock() == null ||
                quantity > product.getStock()) {

            throw new RuntimeException(
                    "Not enough stock"
            );
        }

        cartItem.setQuantity(quantity);
        cartItem.setUpdatedAt(
                LocalDateTime.now()
        );

        cart.setUpdatedAt(LocalDateTime.now());

        return toCartResponse(cart);
    }

    public CartResponse removeItem(
            Long userId,
            Long cartItemId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        CartItem cartItem =
                cartItemRepository.findById(cartItemId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Cart item not found"
                                )
                        );

        // SECURITY CHECK
        if (!cartItem.getCart()
                .getId()
                .equals(cart.getId())) {

            throw new RuntimeException(
                    "Cart item does not belong to this cart"
            );
        }

        cartItemRepository.delete(cartItem);

        cart.setUpdatedAt(LocalDateTime.now());

        return toCartResponse(cart);
    }

    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new RuntimeException("Cart not found")
                );

        cartItemRepository.deleteByCart(cart);

        cart.setUpdatedAt(LocalDateTime.now());
    }


    private Cart getOrCreateCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                   Cart cart = new Cart(user);

                   return cartRepository.save(cart);
                });
    }


    private CartResponse toCartResponse(Cart cart) {

        Optional<CartItem> cartItems =
                cartItemRepository
                        .findByCart(cart);

        List<CartItemResponse> items =
                cartItems.stream()
                        .map(item ->
                                new CartItemResponse(
                                        item.getId(),
                                        cart.getId(),
                                        item.getProduct().getId(),
                                        item.getPrice(),
                                        item.getQuantity(),
                                        item.getCreatedAt(),
                                        item.getUpdatedAt()
                                )
                        )
                        .toList();

        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(),
                items,
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }
}