package com.dat.ecommerce.dto.request;

import com.dat.ecommerce.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderFilterRequest {

    private OrderStatus status;

    private Long userId;

    private String productSku;

    private BigDecimal minTotalAmount;

    private BigDecimal maxTotalAmount;

    private LocalDateTime createdFrom;

    private LocalDateTime createdTo;
}