package ru.skirda.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import ru.skirda.dto.KafkaMessage;

@Service
@RequiredArgsConstructor
public class OwnerResponseService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendResponse(Object payload, String correlationId) {
        kafkaTemplate.send("owner-responses", new KafkaMessage(correlationId, payload));
    }

    public void sendError(String correlationId, String errorMessage) {
        kafkaTemplate.send("owner-responses", new KafkaMessage(correlationId, null, errorMessage));
    }
}
