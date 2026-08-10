package com.dat.ecommerce.service;

import com.dat.ecommerce.repository.OrderItemRepository;
import com.dat.ecommerce.repository.OrderRepository;

public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }


}
