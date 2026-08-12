package com.dat.ecommerce.service;

import com.dat.ecommerce.dto.request.CreatePaymentRequest;
import com.dat.ecommerce.dto.response.PaymentResponse;
import com.dat.ecommerce.dto.response.StripeCheckoutResponse;
import com.dat.ecommerce.entity.Order;
import com.dat.ecommerce.entity.Payment;
import com.dat.ecommerce.enums.OrderStatus;
import com.dat.ecommerce.enums.PaymentStatus;
import com.dat.ecommerce.exception.OrderNotFoundException;
import com.dat.ecommerce.exception.PaymentAlreadyExistsException;
import com.dat.ecommerce.exception.UserNotFoundException;
import com.dat.ecommerce.repository.OrderRepository;
import com.dat.ecommerce.repository.PaymentRepository;
import com.dat.ecommerce.repository.UserRepository;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StripePaymentService stripePaymentService;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository, UserRepository userRepository, StripePaymentService stripePaymentService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.stripePaymentService = stripePaymentService;
    }

    @Transactional
    public StripeCheckoutResponse createStripePayment(
            String email,
            Long orderId
    ) throws StripeException {

        Long userId = getUserIdByEmail(email);

        Order order = orderRepository
                .findByIdAndUserId(orderId, userId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id: " + orderId
                        )
                );

        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            throw new PaymentAlreadyExistsException(
                    "Payment already exists for order: " + orderId
            );
        }

        return stripePaymentService
                .createCheckoutSession(order);
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
