package com.management.patient_service.kafka;

import com.management.patient_service.model.Patient;
import com.management.patient_service.utils.KafkaEventTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

  public void sendEvent(Patient patient) {
    PatientEvent patientEvent =
        PatientEvent.newBuilder()
            .setPatientId(patient.getId().toString())
            .setName(patient.getName())
            .setEmail(patient.getEmail())
            .setEventType(KafkaEventTypes.PATIENT_CREATED.toString())
            .build();
    try {
        kafkaTemplate.send("patient", patientEvent.toByteArray());
    } catch (Exception e) {
      log.error("Error in sending patient created event {}", patientEvent);
    }
  }
}
