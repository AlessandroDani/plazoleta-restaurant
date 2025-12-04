package com.pragma.powerup.infrastructure.exceptionhandler;

import com.pragma.powerup.domain.exception.PlateNotFoundException;
import com.pragma.powerup.domain.exception.RestaurantAlreadyExistException;
import com.pragma.powerup.domain.exception.RestaurantNotExistException;
import com.pragma.powerup.domain.exception.UserIsNotOwnerRestaurantException;
import com.pragma.powerup.infrastructure.exception.InvalidRoleException;
import com.pragma.powerup.infrastructure.exception.OwnerNotFoundException;
import com.pragma.powerup.infrastructure.exception.UserServiceCommunicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor {

    private static final String MESSAGE = "message";

    @ExceptionHandler(OwnerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOwnerNotFoundException(OwnerNotFoundException ignoredOwnerNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap(MESSAGE, ExceptionResponse.NO_DATA_FOUND.getMessage()));
    }

    @ExceptionHandler(InvalidRoleException.class)
    public  ResponseEntity<Map<String, String>> handleInvalidRoleException(InvalidRoleException ignoredInvalidRoleException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap(MESSAGE, ExceptionResponse.USER_DENIED_PERMISSION.getMessage()));
    }

    @ExceptionHandler(UserServiceCommunicationException.class)
    public ResponseEntity<Map<String, String>> handleUserServiceCommunicationException(UserServiceCommunicationException ignoredUserServiceCommunicationException) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.singletonMap(MESSAGE, ExceptionResponse.SERVICE_UNAVAILABLE.getMessage()));
    }

    @ExceptionHandler(RestaurantAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantAlreadyExistException(RestaurantAlreadyExistException  ignoredRestaurantAlreadyExistException) {
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, ExceptionResponse.RESTAURANT_ALREADY_EXIST.getMessage()));
    }

    @ExceptionHandler(UserIsNotOwnerRestaurantException.class)
    public ResponseEntity<Map<String, String>> handleUserIsNotOwnerRestaurantException(UserIsNotOwnerRestaurantException  ignoredUserIsNotOwnerRestaurantException) {
        return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap(MESSAGE, ExceptionResponse.USER_DENIED_PERMISSION.getMessage()));
    }

    @ExceptionHandler(RestaurantNotExistException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantDoesNotExistException(RestaurantNotExistException ignoredRestaurantDoesNotExistException) {
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap(MESSAGE, ExceptionResponse.RESTAURANT_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(PlateNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePlateNotFoundException(PlateNotFoundException ignoredPlateNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap(MESSAGE, ExceptionResponse.PLATE_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        errors.put("timestamp", LocalDateTime.now());
        errors.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}