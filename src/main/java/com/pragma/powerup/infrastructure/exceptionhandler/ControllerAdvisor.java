package com.pragma.powerup.infrastructure.exceptionhandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.infrastructure.exception.InvalidRoleException;
import com.pragma.powerup.domain.exception.InvalidStatusParameterException;
import com.pragma.powerup.infrastructure.exception.RoleNotFoundException;
import com.pragma.powerup.infrastructure.exception.UserServiceCommunicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOwnerNotFoundException(RoleNotFoundException ignoredOwnerNotFoundException) {
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
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap(MESSAGE, ExceptionResponse.RESTAURANT_ALREADY_EXIST.getMessage()));
    }

    @ExceptionHandler(UserIsNotOwnerRestaurantException.class)
    public ResponseEntity<Map<String, String>> handleUserIsNotOwnerRestaurantException(UserIsNotOwnerRestaurantException  ignoredUserIsNotOwnerRestaurantException) {
        return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap(MESSAGE, ExceptionResponse.USER_NOT_OWNER_RESTAURANT.getMessage()));
    }

    @ExceptionHandler(RestaurantNotExistException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantDoesNotExistException(RestaurantNotExistException ignoredRestaurantDoesNotExistException) {
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap(MESSAGE, ExceptionResponse.RESTAURANT_NOT_EXIST.getMessage()));
    }

    @ExceptionHandler(PlateNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePlateNotFoundException(PlateNotFoundException ignoredPlateNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap(MESSAGE, ExceptionResponse.PLATE_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(PlateAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handlePlateAlreadyExistException(PlateAlreadyExistException ignoredPlateAlreadyExistException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap(MESSAGE, ExceptionResponse.PLATE_ALREADY_EXIST.getMessage()));
    }

    @ExceptionHandler(UserHasActiveOrderException.class)
    public ResponseEntity<Map<String, String>> handleUserHasActiveOrderException(UserHasActiveOrderException ignoredUserHasActiveOrderException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap(MESSAGE, ExceptionResponse.USER_HAS_ACTIVE_ORDER.getMessage()));
    }

    @ExceptionHandler(PlateBelongsToAnotherRestaurantException.class)
    public ResponseEntity<Map<String, String>> handlePlateBelongsToAnotherRestaurantException(PlateBelongsToAnotherRestaurantException ignoredPlateBelongsToAnotherRestaurantException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap(MESSAGE, ExceptionResponse.PLATE_BELONGS_ANOTHER_RESTAURANT.getMessage()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoryNotFoundClass (CategoryNotFoundException ignoredCategoryNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap(MESSAGE, ExceptionResponse.CATEGORY_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(EmployeeNotValidException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeNotValidException (EmployeeNotValidException ignoredEmployeeNotValidException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap(MESSAGE, ExceptionResponse.EMPLOYEE_NOT_VALID.getMessage()));
    }

    @ExceptionHandler(InvalidStatusParameterException.class)
    public ResponseEntity<Map<String, String>> handleInvalidStatusParameterException (InvalidStatusParameterException ignoredInvalidStatusParameterException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, ExceptionResponse.STATUS_NOT_VALID.getMessage()));
    }

    @ExceptionHandler(RestaurantEmployeeExistsException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantEmployeeExistException (RestaurantEmployeeExistsException ignoredRestaurantEmployeeExistsException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap(MESSAGE, ExceptionResponse.USER_IS_ALREADY_EMPLOYEE.getMessage()));
    }

    @ExceptionHandler(OrderNotInPendingStatusException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotInPendingStatusException (OrderNotInPendingStatusException ignoredOrderNotInPendingStatusException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap(MESSAGE, ExceptionResponse.ORDER_NOT_PENDING_STATE.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) cause;

            String fieldName = "un campo";
            if (ife.getPath() != null && !ife.getPath().isEmpty()) {
                fieldName = ife.getPath().get(ife.getPath().size() - 1).getFieldName();
            }

            String invalidValue = String.valueOf(ife.getValue());
            String expectedType = ife.getTargetType().getSimpleName();

            String friendlyMessage = String.format(
                    "Error de formato en el campo '%s'. El valor proporcionado ('%s') no pudo ser convertido al tipo de dato esperado (%s).",
                    fieldName,
                    invalidValue,
                    expectedType
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", friendlyMessage));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("error", "Error en el formato de la petición JSON. Verifique la sintaxis."));
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