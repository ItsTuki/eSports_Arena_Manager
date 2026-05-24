package com.example.rankingservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class) public ResponseEntity<Map<String, String>> status(ResponseStatusException ex) { return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason())); }
    @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<Map<String, String>> validation() { return ResponseEntity.badRequest().body(Map.of("error", "Datos invalidos")); }
}
