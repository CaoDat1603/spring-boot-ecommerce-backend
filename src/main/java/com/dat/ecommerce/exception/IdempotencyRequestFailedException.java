package com.dat.ecommerce.exception;

public class IdempotencyRequestFailedException extends RuntimeException {
    private final int status;
    public IdempotencyRequestFailedException(
            int status,
            String message
    ) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}