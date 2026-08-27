package com.dat.ecommerce.controller;

import com.dat.ecommerce.dto.request.AddCartItemRequest;
import com.dat.ecommerce.dto.request.CartItemFilterRequest;
import com.dat.ecommerce.dto.request.UpdateCartItemRequest;
import com.dat.ecommerce.dto.response.CartItemResponse;
import com.dat.ecommerce.dto.response.CartResponse;
import com.dat.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CartResponse> getCart(
            Authentication authentication
    ) {
        String email = authentication.getName();

        return ResponseEntity.ok(cartService.getCart(email));
    }

    @GetMapping("/items")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<CartItemResponse>> getCarts(
            Authentication authentication,

            @RequestParam(required = false)
            Long productId,

            @RequestParam(required = false)
            String productName,

            @RequestParam(required = false)
            String productSku,

            @RequestParam(required = false)
            BigDecimal priceMin,

            @RequestParam(required = false)
            BigDecimal priceMax,

            @RequestParam(required = false)
            Integer quantityMin,

            @RequestParam(required = false)
            Integer quantityMax,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime createdFrom,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime createdTo,

            @ParameterObject
            @PageableDefault(
                    size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            )
            Pageable pageable
    ) {
        String email = authentication.getName();

        CartItemFilterRequest filter = new CartItemFilterRequest();

        filter.setProductId(productId);
        filter.setNameProduct(productName);
        filter.setSku(productSku);
        filter.setPriceMax(priceMax);
        filter.setPriceMin(priceMin);
        filter.setQuantityMax(quantityMax);
        filter.setQuantityMin(quantityMin);
        filter.setCreatedTo(createdTo);
        filter.setCreatedFrom(createdFrom);

        return ResponseEntity.ok(
                cartService.getCartItems(
                        authentication.getName(),
                        filter,
                        pageable
                )
        );
    }

    @PostMapping("/items")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<CartResponse> addItem(
            Authentication authentication,
            @Valid @RequestBody AddCartItemRequest request
    ) {
        String email = authentication.getName();

        CartResponse response = cartService.addItem(email, request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/items/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> clearCart(
            Authentication authentication
    ) {
        String email = authentication.getName();
        cartService.clearCart(email);
        return ResponseEntity.noContent().build();
    }
}
