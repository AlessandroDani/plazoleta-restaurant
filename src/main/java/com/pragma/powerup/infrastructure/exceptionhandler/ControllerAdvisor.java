package com.pragma.powerup.infrastructure.exceptionhandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.infrastructure.exception.*;
import com.pragma.powerup.domain.exception.InvalidStatusParameterException;
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

    @ExceptionHandler(RestaurantAlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantAlreadyExistException(RestaurantAlreadyExistException  ignoredRestaurantAlreadyExistException) {
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap(MESSAGE, ExceptionResponse.RESTAURANT_ALREADY_EXIST.getMessage()));
    }

    @ExceptionHandler(UserNotAssignRestaurant.class)
    public ResponseEntity<Map<String, String>> handleUserNotAssignRestaurant(UserNotAssignRestaurant  ignoredUserNotAssignRestaurant) {
        return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap(MESSAGE, ExceptionResponse.USER_NOT_ASSIGNED.getMessage()));
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

    @ExceptionHandler(PlateNotAvailableException.class)
    public ResponseEntity<Map<String, String>> handlePlateNotAvailableException(PlateNotAvailableException ignoredPlateBelongsToAnotherRestaurantException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, ExceptionResponse.PLATE_NOT_AVAILABLE.getMessage()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCategoryNotFoundClass (CategoryNotFoundException ignoredCategoryNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap(MESSAGE, ExceptionResponse.CATEGORY_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(EmployeeDoesNotBelongToRestaurantException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeNotValidException (EmployeeDoesNotBelongToRestaurantException ignoredEmployeeDoesNotBelongToRestaurantException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap(MESSAGE, ExceptionResponse.USER_NOT_BELONG_RESTAURANT.getMessage()));
    }

    @ExceptionHandler(InvalidStatusParameterException.class)
    public ResponseEntity<Map<String, String>> handleInvalidStatusParameterException (InvalidStatusParameterException ignoredInvalidStatusParameterException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, ExceptionResponse.STATUS_NOT_VALID.getMessage()));
    }

    @ExceptionHandler(RestaurantEmployeeExistsException.class)
    public ResponseEntity<Map<String, String>> handleRestaurantEmployeeExistException (RestaurantEmployeeExistsException ignoredRestaurantEmployeeExistsException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap(MESSAGE, ExceptionResponse.USER_IS_ALREADY_EMPLOYEE.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFoundException (OrderNotFoundException ignoredOrderNotFoundException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, ExceptionResponse.ORDER_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(OrderNotInPendingStatusException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotInPendingStatusException (OrderNotInPendingStatusException ignoredOrderNotInPendingStatusException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap(MESSAGE, ExceptionResponse.ORDER_NOT_PENDING_STATE.getMessage()));
    }

    @ExceptionHandler(OrderNotInReadyStatusException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotInReadyStatusException (OrderNotInReadyStatusException ignoredOrderNotInReadyStatusException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap(MESSAGE, ExceptionResponse.ORDER_NOT_READY_STATE.getMessage()));
    }

    @ExceptionHandler(OrderNotInPreparationStatusException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotInPreparationStatusException (OrderNotInPreparationStatusException ignoredOrderNotInPreparationStatusException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap(MESSAGE, ExceptionResponse.ORDER_NOT_PREPARATION_STATE.getMessage()));
    }

    @ExceptionHandler(OrderHasIncorrectPinException.class)
    public ResponseEntity<Map<String, String>> handleOrderHasIncorrectPinException (OrderHasIncorrectPinException ignoredOrderHasIncorrectPinException) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap(MESSAGE, ExceptionResponse.ORDER_INCORRECT_PIN.getMessage()));
    }

    @ExceptionHandler(ClientIsNotOrderOwnerException.class)
    public ResponseEntity<Map<String, String>> handleClientIsNotOrderOwnerException (ClientIsNotOrderOwnerException ignoredClientIsNotOrderOwnerException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap(MESSAGE, ExceptionResponse.CLIENT_NOT_OWNER.getMessage()));
    }

    @ExceptionHandler(FailedConnectionTraceabilityException.class)
    public ResponseEntity<Map<String, String>> handleFailedConnectionTraceabilityException (FailedConnectionTraceabilityException ignoredFailedConnectionTraceabilityException) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.singletonMap(MESSAGE, ExceptionResponse.FAILED_CONNECTION_TRACE.getMessage()));
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDataException (InvalidDataException ignoredInvalidDataException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap(MESSAGE, ExceptionResponse.INVALID_DATA_EXCEPTION.getMessage()));
    }

    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleUserAuthenticationException(UserAuthenticationException ignoredUserAuthenticationException) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap(MESSAGE, ExceptionResponse.USER_AUTHENTICATION_EXCEPTION.getMessage()));
    }

    @ExceptionHandler(ActionForbiddenException.class)
    public ResponseEntity<Map<String, String>> handleActionForbiddenException(ActionForbiddenException ignoredActionForbiddenException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN) .body(Collections.singletonMap(MESSAGE, ExceptionResponse.ACTION_FORBIDDEN_EXCEPTION.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND) .body(Collections.singletonMap(MESSAGE, ex.getMessage()));
    }

    @ExceptionHandler(ExternalServiceFailureException.class)
    public ResponseEntity<Map<String, String>> handleExternalServiceFailureException(ExternalServiceFailureException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.singletonMap(MESSAGE, ex.getMessage()));
    }

    @ExceptionHandler(ExternalServiceUnavailableException.class)
    public ResponseEntity<Map<String, String>> handleExternalServiceUnavailableException(ExternalServiceUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.singletonMap(MESSAGE, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleUnexpectedException(Exception ignoredException) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) .body(Collections.singletonMap(MESSAGE, ExceptionResponse.UNEXPECTED_ERROR_EXCEPTION.getMessage()));
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