package com.nerdery.ecommerce.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvalidPasswordException extends RuntimeException {
    private final String message;
}
