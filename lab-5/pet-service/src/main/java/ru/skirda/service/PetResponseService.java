package ru.skirda.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skirda.dto.KafkaMessage;

@Service
@RequiredArgsConstructor
public class PetResponseService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendResponse(Object payload, String correlationId) {
        kafkaTemplate.send("pet-responses", new KafkaMessage(correlationId, payload));
    }

    public void sendError(String correlationId, String errorMessage) {
        kafkaTemplate.send("pet-responses", new KafkaMessage(correlationId, null, errorMessage));
    }
}
