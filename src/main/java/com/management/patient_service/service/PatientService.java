package com.management.patient_service.service;

import com.management.patient_service.dto.PatientRequestDTO;
import com.management.patient_service.dto.PatientResponseDTO;
import com.management.patient_service.expection.EmailExistsException;
import com.management.patient_service.expection.PatientNotFoundException;
import com.management.patient_service.grpc.BillingServiceGrpcClient;
import com.management.patient_service.kafka.KafkaProducer;
import com.management.patient_service.mapper.PatientMapper;
import com.management.patient_service.model.Patient;
import com.management.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

  private final PatientRepository patientRepository;
  private final BillingServiceGrpcClient billingServiceGrpcClient;
  private final KafkaProducer kafkaProducer;

  public PatientService(
      PatientRepository patientRepository,
      BillingServiceGrpcClient billingServiceGrpcClient, KafkaProducer kafkaProducer) {
    this.patientRepository = patientRepository;
    this.billingServiceGrpcClient = billingServiceGrpcClient;
    this.kafkaProducer = kafkaProducer;
  }

  public List<PatientResponseDTO> getPatients() {
    List<Patient> patients = patientRepository.findAll();
    return patients.stream().map(PatientMapper::toPatientResponseDTO).toList();
  }

  public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
    if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
      throw new EmailExistsException("Email already exists " + patientRequestDTO.getEmail());
    }
    Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

    // Creating billing account for the patient
    billingServiceGrpcClient.createBillingAccount(
        patient.getId().toString(), patient.getName(), patient.getEmail(), patient.getAddress());

    // Sending kafka event
    kafkaProducer.sendEvent(patient);

    return PatientMapper.toPatientResponseDTO(patient);
  }

  public PatientResponseDTO updatePatient(UUID patientId, PatientRequestDTO patientRequestDTO) {
    Patient patient = patientRepository.findById(patientId).orElse(null);
    if (patient == null) {
      throw new PatientNotFoundException("Patient not found " + patientId);
    }

    patient.setName(patientRequestDTO.getName());
    patient.setEmail(patientRequestDTO.getEmail());
    patient.setAddress(patientRequestDTO.getAddress());
    patient.setDob(LocalDate.parse(patientRequestDTO.getDob()));

    Patient updatedPatient = patientRepository.save(patient);

    return PatientMapper.toPatientResponseDTO(updatedPatient);
  }

  public PatientResponseDTO deletePatient(UUID patientId) {
    Patient patient = patientRepository.findById(patientId).orElse(null);
    if (patient == null) {
      throw new PatientNotFoundException("Patient not found " + patientId);
    }
    patientRepository.delete(patient);

    return PatientMapper.toPatientResponseDTO(patient);
  }
}
