package com.dat.ecommerce.service;

import com.dat.ecommerce.dto.request.AddCartItemRequest;
import com.dat.ecommerce.dto.request.CartItemFilterRequest;
import com.dat.ecommerce.dto.request.UpdateCartItemRequest;
import com.dat.ecommerce.dto.response.CartItemResponse;
import com.dat.ecommerce.dto.response.CartResponse;
import com.dat.ecommerce.entity.Cart;
import com.dat.ecommerce.entity.CartItem;
import com.dat.ecommerce.entity.Product;
import com.dat.ecommerce.entity.User;
import com.dat.ecommerce.exception.CartItemNotFoundException;
import com.dat.ecommerce.exception.InsufficientStockException;
import com.dat.ecommerce.exception.ProductNotFoundException;
import com.dat.ecommerce.exception.UserNotFoundException;
import com.dat.ecommerce.repository.CartItemRepository;
import com.dat.ecommerce.repository.CartRepository;
import com.dat.ecommerce.repository.ProductRepository;
import com.dat.ecommerce.repository.UserRepository;
import com.dat.ecommerce.specification.CartItemSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public CartResponse getCart(String email) {

        User user = getUserByEmail(email);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> createCart(user));

        List<CartItemResponse> items =
                cartItemRepository.findByCart(cart)
                        .stream()
                        .map(CartItemResponse::new)
                        .toList();

        return new CartResponse(cart, items);
    }

    @Transactional(readOnly = true)
    public Page<CartItemResponse> getCartItems(
            String email,
            CartItemFilterRequest filter,
            Pageable pageable
    ) {
        User user = getUserByEmail(email);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> createCart(user));

        Specification<CartItem> specification =
                (root, query, criteriaBuilder) -> null;

        if (cart.getId() == null) {
            return Page.empty();
        } else {
            specification = specification.and(
                    CartItemSpecification.hasCartId(
                            cart.getId()
                    )
            );
        }

        if (filter.getProductId() != null) {
            specification = specification.and(
                    CartItemSpecification.hasProductId(
                            filter.getProductId()
                    )
            );
        }

        if (filter.getNameProduct() != null) {
            specification = specification.and(
                    CartItemSpecification.hasProductName(
                            filter.getNameProduct()
                    )
            );
        }

        if (filter.getSku() != null) {
            specification = specification.and(
                    CartItemSpecification.hasProductSku(
                            filter.getSku()
                    )
            );
        }

        if (filter.getPriceMin() != null) {
            specification = specification.and(
                    CartItemSpecification.priceGreaterThanOrEqual(
                            filter.getPriceMin()
                    )
            );
        }

        if (filter.getPriceMax() != null) {
            specification = specification.and(
                    CartItemSpecification.priceLessThanOrEqual(
                            filter.getPriceMax()
                    )
            );
        }

        if (filter.getQuantityMin() != null) {
            specification = specification.and(
                    CartItemSpecification.quantityGreaterThanOrEqual(
                            filter.getQuantityMin()
                    )
            );
        }

        if (filter.getQuantityMax() != null) {
            specification = specification.and(
                    CartItemSpecification.quantityLessThanOrEqual(
                            filter.getQuantityMax()
                    )
            );
        }

        if (filter.getCreatedFrom() != null) {
            specification = specification.and(
                    CartItemSpecification.createdAtGreaterThanOrEqual(
                            filter.getCreatedFrom()
                    )
            );
        }

        if (filter.getCreatedTo() != null) {
            specification = specification.and(
                    CartItemSpecification.createdAtLessThanOrEqual(
                            filter.getCreatedTo()
                    )
            );
        }

        Page<CartItem> cartItems =
                cartItemRepository.findAll(
                        specification,
                        pageable
                );

        return cartItems.map(CartItemResponse::new);
    }

    @Transactional
    public CartResponse addItem(
            String email,
            AddCartItemRequest request
    ) {

        User user = getUserByEmail(email);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> createCart(user));


        Product product = productRepository.findById(
                        request.getProductId()
                )
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: "
                                        + request.getProductId()
                        )
                );

        if (request.getQuantity() > product.getStock()) {
            throw new IllegalArgumentException(
                    "Not enough product stock"
            );
        }

        CartItem cartItem =
                cartItemRepository
                        .findByCartAndProduct(cart, product)
                        .orElse(null);


        if (cartItem != null) {

            int newQuantity = cartItem.getQuantity() + request.getQuantity();

            if (newQuantity > product.getStock()) {
                throw new IllegalArgumentException(
                        "Not enough product stock"
                );
            }

            cartItem.setQuantity(newQuantity);

            cartItem.setPrice(product.getPrice());

            cartItemRepository.save(cartItem);

        } else {

            CartItem newItem = new CartItem(
                    cart,
                    product,
                    product.getPrice(),
                    request.getQuantity());

            cartItemRepository.save(newItem);
        }

        return getCart(email);
    }

    @Transactional
    public CartResponse updateItem(
            String email,
            Long productId,
            UpdateCartItemRequest request
    ) {

        User user = getUserByEmail(email);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new CartItemNotFoundException(
                                "Cart not found"
                        )
                );

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: "
                                        + productId
                        )
                );

        CartItem cartItem =
                cartItemRepository
                        .findByCartAndProduct(cart, product)
                        .orElseThrow(() ->
                                new CartItemNotFoundException(
                                        "Product is not in cart"
                                )
                        );

        if (request.getQuantity() > product.getStock()) {
            throw new InsufficientStockException(
                    "Not enough product stock"
            );
        }


        cartItem.setQuantity(request.getQuantity());

        cartItem.setPrice(product.getPrice());

        cartItemRepository.save(cartItem);

        return getCart(email);
    }

    @Transactional
    public CartResponse removeItem(
            String email,
            Long productId
    ) {

        User user = getUserByEmail(email);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new CartItemNotFoundException(
                                "Cart not found"
                        )
                );

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + productId
                        )
                );

        CartItem cartItem =
                cartItemRepository
                        .findByCartAndProduct(cart, product)
                        .orElseThrow(() ->
                                new CartItemNotFoundException(
                                        "Product is not in cart"
                                )
                        );

        cartItemRepository.delete(cartItem);
        return getCart(email);
    }

    @Transactional
    public void clearCart(String email) {

        User user = getUserByEmail(email);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElse(null);

        if (cart == null) {
            return;
        }

        cartItemRepository.deleteByCart(cart);
    }



    private User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with email: " + email
                        )
                );
    }


    private Cart createCart(User user) {

        Cart cart = new Cart(user);

        return cartRepository.save(cart);
    }
}