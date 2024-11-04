package com.ing_software.tp.advice;

import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> invalidRegisterRequest(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameNotFoundException.class)
    public Map<String, String> incorrectLoginCredentialsException(UsernameNotFoundException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", exception.getMessage());
        return error;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, String> noBodyLoginRequestException(HttpMessageNotReadableException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "The request requires a body with login credentials");
        return error;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> invalidOrderException(RuntimeException exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", exception.getMessage());
        return error;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        String message = "El email o el nombre de usuario ya están en uso. Por favor, intenta con otros.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.class)
    public Map<String, String> noProductsFoundException(Exception exception){
        Map<String, String> error = new HashMap<>();
        error.put("error", exception.getMessage());
        return error;
    }
}
