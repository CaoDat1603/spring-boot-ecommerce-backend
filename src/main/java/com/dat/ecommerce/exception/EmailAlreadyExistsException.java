package com.dat.ecommerce.exception;

// Error email đã tồn tại
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}

