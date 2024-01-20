package com.ecommerce.demo.ecommerce.backend.exception;

public class ResourceNotFoundException extends Throwable {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
