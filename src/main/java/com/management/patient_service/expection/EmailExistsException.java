package com.management.patient_service.expection;

public class EmailExistsException extends RuntimeException {
  public EmailExistsException(String message) {
    super(message);
  }
}
