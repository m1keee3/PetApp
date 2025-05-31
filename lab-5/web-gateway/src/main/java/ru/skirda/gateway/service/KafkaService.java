package ru.skirda.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.skirda.dto.KafkaMessage;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class KafkaService {

    private final KafkaTemplate<String, KafkaMessage> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final Map<String, CompletableFuture<Object>> pendingRequests = new ConcurrentHashMap<>();

    public <T> CompletableFuture<T> sendRequest(String topic, String command, Object payload, Class<T> responseType) {
        String correlationId = UUID.randomUUID().toString();
        KafkaMessage message = new KafkaMessage(correlationId, payload);

        CompletableFuture<Object> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);

        kafkaTemplate.send(topic, command, message)
                .whenComplete((res, ex) -> {
                    if (ex != null) {
                        pendingRequests.remove(correlationId);
                        future.completeExceptionally(ex);
                    }
                });

        return future.thenApply(obj -> objectMapper.convertValue(obj, responseType));
    }

    @KafkaListener(topics = "owner-responses")
    public void handleOwnerResponse(KafkaMessage message) {
        handleResponse(message);
    }

    @KafkaListener(topics = "pet-responses")
    public void handlePetResponse(KafkaMessage message) {
        handleResponse(message);
    }

    private void handleResponse(KafkaMessage message) {
        CompletableFuture<Object> future = pendingRequests.remove(message.getCorrelationId());
        if (future != null) {
            if (message.getError() != null) {
                future.completeExceptionally(new RuntimeException(message.getError()));
            } else {
                future.complete(message.getData());
            }
        }
    }
}