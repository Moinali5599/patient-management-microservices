package com.management.patient_service.controller;

import com.management.patient_service.dto.PatientRequestDTO;
import com.management.patient_service.dto.PatientResponseDTO;
import com.management.patient_service.dto.validators.CreatePatientValidationGroup;
import com.management.patient_service.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patient management", description = "CRUD API for patient management system")
public class PatientController {

  private final PatientService patientService;

  public PatientController(PatientService patientService) {
    this.patientService = patientService;
  }

  @GetMapping()
  @Operation(summary = "Retrieve all patient records")
  public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
    return new ResponseEntity<>(patientService.getPatients(), HttpStatus.OK);
  }

  @PostMapping()
  @Operation(summary = "Creat new patient record")
  public ResponseEntity<PatientResponseDTO> createPatient(
      @Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
    return new ResponseEntity<>(
        patientService.createPatient(patientRequestDTO), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a patient record with id")
  public ResponseEntity<PatientResponseDTO> updatePatient(
      @PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
    return new ResponseEntity<>(patientService.updatePatient(id, patientRequestDTO), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a patient record with id")
  public ResponseEntity<PatientResponseDTO> deletePatient(@PathVariable UUID id) {
    return new ResponseEntity<>(patientService.deletePatient(id), HttpStatus.OK);
  }
}
