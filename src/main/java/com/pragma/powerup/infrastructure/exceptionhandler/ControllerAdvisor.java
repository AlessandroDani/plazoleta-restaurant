package com.pragma.powerup.infrastructure.exceptionhandler;

import com.pragma.powerup.infrastructure.exception.InvalidRoleException;
import com.pragma.powerup.infrastructure.exception.OwnerNotFoundException;
import com.pragma.powerup.infrastructure.exception.UserServiceCommunicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
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
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonMap(MESSAGE, ExceptionResponse.FORBIDDEN.getMessage()));
    }

    @ExceptionHandler(UserServiceCommunicationException.class)
    public ResponseEntity<Map<String, String>> handleUserServiceCommunicationException(UserServiceCommunicationException ignoredUserServiceCommunicationException) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Collections.singletonMap(MESSAGE, ExceptionResponse.SERVICE_UNAVAILABLE.getMessage()));
    }

}