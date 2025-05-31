package ru.skirda.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KafkaMessage {
    private String correlationId;
    private Object data;
    private String error;

    public KafkaMessage(String correlationId, Object data) {
        this.correlationId = correlationId;
        this.data = data;
    }

    public KafkaMessage(String correlationId, Object o, String errorMessage) {
        this.correlationId = correlationId;
        this.data = o;
        this.error = errorMessage;
    }
}
