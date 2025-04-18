package com.management.patient_service.utils;

public enum KafkaEventTypes {
    PATIENT_CREATED("PATIENT_CREATED"),
    PATIENT_UPDATED("PATIENT_UPDATED"),
    PATIENT_DELETED("PATIENT_DELETED");

    private final String eventType;

    KafkaEventTypes(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return eventType;
    }
}

