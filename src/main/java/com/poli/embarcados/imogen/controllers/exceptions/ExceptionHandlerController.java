package com.poli.embarcados.imogen.controllers.exceptions;

import com.poli.embarcados.imogen.services.exceptions.ExistingDataException;
import com.poli.embarcados.imogen.services.exceptions.InvalidDataException;
import com.poli.embarcados.imogen.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(ExistingDataException.class)
    public ResponseEntity<StandardException> handleExistingDataException(ExistingDataException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        StandardException exception = new StandardException();
        exception.setTimestamp(Instant.now());
        exception.setStatus(status.value());
        exception.setError("Existing Data Exception");
        exception.setMessage(e.getMessage());
        exception.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError();
        err.setTimestamp(Instant.now());
        err.setStatus(status.value());
        err.setError("Validation Exception");
        err.setMessage(e.getMessage());
        err.setPath(request.getRequestURI());
        for(FieldError f: e.getBindingResult().getFieldErrors()) {
            err.getErrors().add(new FieldMessage(f.getField(), f.getDefaultMessage()));
        }
        return ResponseEntity.status(status).body(err);
    }
}
