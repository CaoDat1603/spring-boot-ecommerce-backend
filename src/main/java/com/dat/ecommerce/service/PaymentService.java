package com.dat.ecommerce.service;

import com.dat.ecommerce.dto.request.CreatePaymentRequest;
import com.dat.ecommerce.dto.response.PaymentResponse;
import com.dat.ecommerce.entity.Order;
import com.dat.ecommerce.entity.Payment;
import com.dat.ecommerce.enums.OrderStatus;
import com.dat.ecommerce.enums.PaymentStatus;
import com.dat.ecommerce.exception.OrderNotFoundException;
import com.dat.ecommerce.exception.UserNotFoundException;
import com.dat.ecommerce.repository.OrderRepository;
import com.dat.ecommerce.repository.PaymentRepository;
import com.dat.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PaymentResponse createPayment(
            String email,
            CreatePaymentRequest request
    ) {
        Order order = orderRepository
                .findByIdAndUserId(
                        request.getOrderId(),
                        getUserIdByEmail(email)
                ).orElseThrow(() ->
                        new OrderNotFoundException("Order not found with id: " + request.getOrderId())
                );

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Order is already completed"
            );
        }

        if (paymentRepository.findByOrderId(order.getId()).isPresent()) {
            throw new IllegalStateException(
                    "Payment already exists for this order"
            );
        }

        Payment payment = new Payment(
                order,
                order.getTotalAmount(),
                PaymentStatus.PENDING,
                request.getMethod()
        );

        payment.setPaidAt(null);

        Payment savePayment = paymentRepository.save(payment);

        return new PaymentResponse(savePayment);
    }

    @Transactional
    public PaymentResponse confirmPayment(
            String email,
            Long paymentId
    ) {
        Payment payment = paymentRepository
                .findById(paymentId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Payment not found with id: " + paymentId
                        )
                );

        Order order = payment.getOrder();

        if (!order.getUser().getEmail().equals(email)) {
            throw new IllegalStateException(
                    "You do not have permission to confirm this payment"
            );
        }

        if (payment.getStatus() == PaymentStatus.PAID) {
            return new PaymentResponse(payment);
        }

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Payment cannot be confirmed"
            );
        }

        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());

        order.setStatus(OrderStatus.COMPLETED);

        orderRepository.save(order);

        Payment savePayment = paymentRepository.save(payment);

        return new PaymentResponse(payment);
    }

    public List<PaymentResponse> getAlPayment() {
        return paymentRepository.findAll()
                .stream()
                .map(PaymentResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(
            String email,
            Long orderId
    ) {
        Order order = orderRepository
                .findByIdAndUserId(
                        orderId,
                        getUserIdByEmail(email)
                ).orElseThrow(() ->
                        new UserNotFoundException(
                                "Order not found with id: " + orderId
                        )
                );

        Payment payment = paymentRepository
                .findByOrderId(order.getId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Payment not found"
                        )
                );

        return new PaymentResponse(payment);
    }

    private Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with email: " + email)
                ).getId();
    }
}
