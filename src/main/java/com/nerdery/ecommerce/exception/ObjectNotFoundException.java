package com.nerdery.ecommerce.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String message){
        super(message);
    }
}
