package com.dat.ecommerce.exception;

public class IdempotencyInProgressException         extends RuntimeException {
    public IdempotencyInProgressException(String message) {
        super(message);
    }
}
