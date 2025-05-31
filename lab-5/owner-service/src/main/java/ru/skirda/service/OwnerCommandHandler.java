package ru.skirda.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import ru.skirda.dto.KafkaMessage;
import ru.skirda.dto.OwnerDto;
import ru.skirda.model.Owner;
import ru.skirda.repository.OwnerRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OwnerCommandHandler {
    private final OwnerRepository repository;
    private final ObjectMapper mapper;
    private final OwnerResponseService responseService;

    @KafkaListener(topics = "owner-requests", groupId = "owner-service-group")
    public void handleCommand(KafkaMessage message, @Header(KafkaHeaders.RECEIVED_KEY) String command) {
        Object data = message.getData();

        try {
            switch (command) {
                case "createOwner" -> createOwner(convert(data), message.getCorrelationId());
                case "getOwnerById" -> getOwnerById(((Map<?, ?>) data).get("id"), message.getCorrelationId());
                case "updateOwner" -> updateOwner(convert(data), message.getCorrelationId());
                case "deleteOwner" -> deleteOwner(convert(data), message.getCorrelationId());
                case "getAllOwners" -> getAllOwners(convert(data), message.getCorrelationId());
                default -> responseService.sendError(message.getCorrelationId(), "Unknown command: " + command);
            }
        } catch (Exception e) {
            responseService.sendError(message.getCorrelationId(), "Error: " + e.getMessage());
        }
    }

    private OwnerDto convert(Object raw) {
        return mapper.convertValue(raw, OwnerDto.class);
    }

    private void createOwner(OwnerDto dto, String correlationId) {
        Owner owner = new Owner(dto.getName(), dto.getBirthDate(), dto.getUserId());
        repository.save(owner);
        responseService.sendResponse(ownerToDto(owner), correlationId);
    }

    private void getOwnerById(Object idRaw, String correlationId) {
        Long id = Long.valueOf(idRaw.toString());
        repository.findById(id)
                .ifPresentOrElse(
                        owner -> responseService.sendResponse(ownerToDto(owner), correlationId),
                        () -> responseService.sendError(correlationId, "Owner not found with id: " + id)
                );
    }

    private void updateOwner(OwnerDto dto, String correlationId) {
        repository.findById(dto.getId())
                .ifPresentOrElse(owner -> {
                    owner.setName(dto.getName());
                    owner.setBirthDate(dto.getBirthDate());
                    repository.save(owner);
                    responseService.sendResponse(ownerToDto(owner), correlationId);
                }, () -> responseService.sendError(correlationId, "Owner not found with id: " + dto.getId()));
    }

    private void deleteOwner(OwnerDto dto, String correlationId) {
        repository.findById(dto.getId())
                .ifPresentOrElse(owner -> {
                    if (!owner.getUserId().equals(dto.getUserId())) {
                        responseService.sendError(correlationId, "Permission denied");
                        return;
                    }
                    repository.delete(owner);
                    responseService.sendResponse(null, correlationId);
                }, () -> responseService.sendError(correlationId, "Owner not found with id: " + dto.getId()));
    }

    private void getAllOwners(OwnerDto filter, String correlationId) {
        Page<Owner> page = repository.findAll(PageRequest.of(filter.getPage(), filter.getSize()));
        responseService.sendResponse(page.map(this::ownerToDto), correlationId);
    }

    private OwnerDto ownerToDto(Owner owner) {
        return new OwnerDto(owner.getId(), owner.getName(), owner.getBirthDate(), owner.getUserId());
    }
}
