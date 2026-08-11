package com.dat.ecommerce.controller;

import com.dat.ecommerce.dto.request.AddCartItemRequest;
import com.dat.ecommerce.dto.request.UpdateCartItemRequest;
import com.dat.ecommerce.dto.response.CartResponse;
import com.dat.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(cartService.getCart(email));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(
            Authentication authentication,
            @Valid @RequestBody AddCartItemRequest request
    ) {
        String email = authentication.getName();

        CartResponse response = cartService.addItem(email, request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartResponse> updateItem(
            Authentication authentication,
            @PathVariable Long proudctId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        String email = authentication.getName();

        CartResponse response = cartService.updateItem(email, proudctId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponse> removeItem(
            Authentication authentication,
            @PathVariable Long productId
    ) {
        String email = authentication.getName();
        return ResponseEntity.ok(
                cartService.removeItem( email, productId )
        );
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            Authentication authentication
    ) {
        String email = authentication.getName();
        cartService.clearCart(email);
        return ResponseEntity.noContent().build();
    }
}
