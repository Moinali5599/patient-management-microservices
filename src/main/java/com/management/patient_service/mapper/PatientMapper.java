package com.management.patient_service.mapper;

import com.management.patient_service.dto.PatientRequestDTO;
import com.management.patient_service.dto.PatientResponseDTO;
import com.management.patient_service.model.Patient;

import java.time.LocalDate;

public class PatientMapper {
    public static PatientResponseDTO toPatientResponseDTO(Patient patient) {
        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(patient.getId().toString());
        patientResponseDTO.setEmail(patient.getEmail());
        patientResponseDTO.setName(patient.getName());
        patientResponseDTO.setAddress(patient.getAddress());
        patientResponseDTO.setDob(patient.getDob().toString());

        return patientResponseDTO;
    }

    public static Patient toModel(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDob(LocalDate.parse(patientRequestDTO.getDob()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDTO .getRegisteredDate()));

        return patient;
    }
}
