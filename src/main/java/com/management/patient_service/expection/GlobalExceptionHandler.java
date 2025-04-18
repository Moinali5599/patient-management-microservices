package com.management.patient_service.expection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidException(
      MethodArgumentNotValidException ex) {
    Map<String, String> errorMap = new HashMap<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));

    return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EmailExistsException.class)
  public ResponseEntity<Map<String, String>> handleEmailExistsException(EmailExistsException ex) {
    log.warn("Email already exists {}", ex.getMessage());
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("message", "Email already exists");
    return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(PatientNotFoundException.class)
  public ResponseEntity<Map<String, String>> handlePatientNotFoundException(PatientNotFoundException ex) {
    log.warn("Patient with id {} is not found", ex.getMessage());
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("message", "Patient is not found");
    return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
  }
}
