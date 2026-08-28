package com.mani.ecommerce.Exceptions;

public class APIException extends RuntimeException {                // used for handle the duplicate category

    private static final long serialVersionUID = 1L;

    public APIException(){

    }
    public APIException(String message) {
        super(message);
    }
}
