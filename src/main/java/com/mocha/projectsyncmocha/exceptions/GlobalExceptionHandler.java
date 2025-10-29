package com.capuccino.projectsynccapuccino.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleNotFound(EntityNotFoundException ex){
        Map<String, Object> error = new HashMap<>();
        error.put("error",ex.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidation(MethodArgumentNotValidException ex){
        Map<String,Object> error = new HashMap<>();
        error.put("error","Validation failed");
        error.put("details",ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": "+f.getDefaultMessage()).toList());
        error.put("status",HttpStatus.BAD_REQUEST.value());
        error.put("timestamp",LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String,Object>> handleDataIntegrity(DataIntegrityViolationException ex){
        Map<String,Object> error = new HashMap<>();
        error.put("error","Data constrait violation");
        error.put("message", ex.getMostSpecificCause().getMessage());
        error.put("status", HttpStatus.CONFLICT.value());
        error.put("timestamp",LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleGeneral(Exception ex){
        Map<String,Object> error = new HashMap<>();
        error.put("error","Unexpected error");
        error.put("message",ex.getMessage());
        error.put("status",HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("timestamp",LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
