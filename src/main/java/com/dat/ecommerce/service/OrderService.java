package com.dat.ecommerce.service;

import com.dat.ecommerce.dto.request.OrderFilterRequest;
import com.dat.ecommerce.dto.response.OrderItemResponse;
import com.dat.ecommerce.dto.response.OrderResponse;
import com.dat.ecommerce.entity.*;
import com.dat.ecommerce.enums.OrderStatus;
import com.dat.ecommerce.enums.Role;
import com.dat.ecommerce.exception.*;
import com.dat.ecommerce.repository.*;
import com.dat.ecommerce.specification.OrderSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponse createOrder(String email) {
        User user = getUserByEmail(email);

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new CartNotFoundException("Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new EmptyCartException( "Cannot create order from empty cart" );
        }

        Order order = new Order(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(BigDecimal.ZERO);

        Order savedOrder = orderRepository.save(order);
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product == null) {
                throw new ProductNotFoundException("Product not found");
            }

            if (product.getStock() < cartItem.getQuantity()) {
                throw new InsufficientStockException(
                        "Not enough stock for product: " + product.getName());
            }

            BigDecimal currentPrice = product.getPrice();

            BigDecimal subtotal = currentPrice.multiply(
                    BigDecimal.valueOf(cartItem.getQuantity())
            );

            OrderItem orderItem = new OrderItem(
                    savedOrder,
                    product,
                    cartItem.getQuantity()
            );
            orderItemRepository.save(orderItem);

            product.setStock( product.getStock() - cartItem.getQuantity() );
            productRepository.save(product);

            totalAmount = totalAmount.add(subtotal);
        }

        savedOrder.setTotalAmount(totalAmount);

        Order finalOrder = orderRepository.save(savedOrder);

        cartItemRepository.deleteByCart(cart);

        List<OrderItemResponse> items =
                orderItemRepository.findByOrder(finalOrder)
                        .stream().map(OrderItemResponse::new).toList();

        return new OrderResponse(
                finalOrder,
                items
        );
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(String email) {
        User user = getUserByEmail(email);
        return orderRepository.findByUserIdOrderByCreatedAtDesc(
                user.getId()
        ).stream()
                .map(this::toOrderResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toOrderResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById( String email, Long orderId ) {
        User user = getUserByEmail(email);
        Order order = orderRepository.findByIdAndUserId(
                orderId,
                user.getId()
        ).orElseThrow(() ->
                new OrderNotFoundException( "Order not found" + orderId));

        return toOrderResponse(order);
    }



    private User getUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with email: " + email)
                );
    }

    public Page<OrderResponse> getOrders(
            String email,
            OrderFilterRequest filter,
            Pageable pageable
    ) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        )
                );

        Specification<Order> specification =
                (root, query, cb) -> null;

        if (user.getRole() == Role.USER) {
            specification = specification.and(
                    OrderSpecification.hasUserId(
                            user.getId()
                    )
            );

        } else if (user.getRole() == Role.ADMIN) {
            if (filter.getUserId() != null) {
                specification = specification.and(
                        OrderSpecification.hasUserId(
                                filter.getUserId()
                        )
                );
            }
        }

        if (filter.getStatus() != null) {

            specification = specification.and(
                    OrderSpecification.hasStatus(
                            filter.getStatus()
                    )
            );
        }

        if (filter.getMinTotalAmount() != null) {

            specification = specification.and(
                    OrderSpecification
                            .totalAmountGreaterThanOrEqual(
                                    filter.getMinTotalAmount()
                            )
            );
        }

        if (filter.getMaxTotalAmount() != null) {

            specification = specification.and(
                    OrderSpecification
                            .totalAmountLessThanOrEqual(
                                    filter.getMaxTotalAmount()
                            )
            );
        }

        if (filter.getCreatedFrom() != null) {

            specification = specification.and(
                    OrderSpecification
                            .createdAtGreaterThanOrEqual(
                                    filter.getCreatedFrom()
                            )
            );
        }

        if (filter.getCreatedTo() != null) {

            specification = specification.and(
                    OrderSpecification
                            .createdAtLessThanOrEqual(
                                    filter.getCreatedTo()
                            )
            );
        }

        if (filter.getProductSku() != null && filter.getProductSku().isBlank()) {
            specification = specification.and(
                    OrderSpecification.hasProductSku(
                            filter.getProductSku()
                    )
            );
        }

        Page<Order> orders =
                orderRepository.findAll(
                        specification,
                        pageable
                );

        return orders.map(this::toOrderResponse);
    }

    private OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> items =
                orderItemRepository
                        .findByOrder(order)
                        .stream()
                        .map(OrderItemResponse::new)
                        .toList();

        return new OrderResponse(
                order,
                items
        );
    }
}
