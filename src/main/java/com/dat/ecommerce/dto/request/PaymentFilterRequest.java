package com.dat.ecommerce.dto.request;

import com.dat.ecommerce.enums.PaymentMethod;
import com.dat.ecommerce.enums.PaymentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PaymentFilterRequest {
    private Long UserId;

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private BigDecimal minAmount;

    private BigDecimal maxAmount;

    private LocalDateTime createdFrom;

    private LocalDateTime createdTo;

    private LocalDateTime paidFrom;

    private LocalDateTime paidTo;
}
