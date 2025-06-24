package com.project.HotelBookingApp.advice;

import com.project.HotelBookingApp.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiError handleResourceNotFoundException(String message){
        return ApiError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .error(message)
                .build();
    }
}
