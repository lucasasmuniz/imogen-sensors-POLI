package com.poli.embarcados.imogen.controllers.exceptions;

import com.poli.embarcados.imogen.services.exceptions.InvalidDataException;
import com.poli.embarcados.imogen.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardException> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardException exception = new StandardException();
        exception.setTimestamp(Instant.now());
        exception.setStatus(status.value());
        exception.setError("Entity Exception");
        exception.setMessage(e.getMessage());
        exception.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(exception);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<StandardException> handleInvalidDataException(InvalidDataException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        StandardException exception = new StandardException();
        exception.setTimestamp(Instant.now());
        exception.setStatus(status.value());
        exception.setError("Invalid Data Exception");
        exception.setMessage(e.getMessage());
        exception.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(exception);
    }
}
