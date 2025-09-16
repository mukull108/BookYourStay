package com.project.HotelBookingApp.advice;

import com.project.HotelBookingApp.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException exception){
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .build();
        return buildApiErrorResponseEntity(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleSpringSecurityAuthenticationExceptions(AuthenticationException exception){
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
    }

    //this will not work directly from here,for this we will have to pass exception from filter chain context to servlet,
    // global exception handler will only handle exceptions on servlet context
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException exception){
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
    }

    //when we do not have access, if you are not using this then your request will take you to the login page, after
    //adding this you now get forbidden error because you do not have access.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException exception){
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(apiError,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServerError(Exception exception) {
        ApiError apiError = ApiError.builder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(exception.getMessage())
                .build();
        return buildApiErrorResponseEntity(apiError);
    }

    public ResponseEntity<ApiResponse<?>> buildApiErrorResponseEntity(ApiError apiError)
    {
        return new ResponseEntity<>(new ApiResponse<>(apiError),apiError.getHttpStatus());

    }
}
