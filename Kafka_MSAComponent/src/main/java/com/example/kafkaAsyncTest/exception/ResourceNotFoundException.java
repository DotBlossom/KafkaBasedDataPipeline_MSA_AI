package com.example.kafkaAsyncTest.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String message;
    private final HttpStatus httpStatus;


    public ResourceNotFoundException(String message) {
        //417
        this(message, HttpStatus.EXPECTATION_FAILED);
    }

    public ResourceNotFoundException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}