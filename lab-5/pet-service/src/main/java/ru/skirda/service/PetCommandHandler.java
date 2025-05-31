package ru.skirda.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import ru.skirda.dto.KafkaMessage;
import ru.skirda.dto.PetDto;
import ru.skirda.model.Pet;
import ru.skirda.repository.PetRepository;

@Service
@RequiredArgsConstructor
public class PetCommandHandler {
    private final PetRepository repository;
    private final PetResponseService responseService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "pet-requests", groupId = "pet-service-group")
    public void handleCommand(KafkaMessage message, @Header(KafkaHeaders.RECEIVED_KEY) String command) {
        Object raw = message.getData();

        try {
            switch (command) {
                case "createPet" -> createPet(convert(raw), message.getCorrelationId());
                case "getPetById" -> getPetById(convert(raw), message.getCorrelationId());
                case "updatePet" -> updatePet(convert(raw), message.getCorrelationId());
                case "deletePet" -> deletePet(convert(raw), message.getCorrelationId());
                default -> responseService.sendError(message.getCorrelationId(), "Unknown command: " + command);
            }
        } catch (Exception e) {
            responseService.sendError(message.getCorrelationId(), "Error processing command: " + e.getMessage());
        }
    }

    private PetDto convert(Object raw) {
        return objectMapper.convertValue(raw, PetDto.class);
    }

    private void createPet(PetDto dto, String correlationId) {
        Pet pet = new Pet(dto.getName(), dto.getBreed(), dto.getColor(), dto.getBirthDate(), dto.getOwnerId());
        repository.save(pet);
        responseService.sendResponse(petToDto(pet), correlationId);
    }

    private void getPetById(PetDto dto, String correlationId) {
        Long id = dto.getId();
        Long requesterOwnerId = dto.getOwnerId();

        repository.findById(id)
                .ifPresentOrElse(pet -> {
                    if (!pet.getOwnerId().equals(requesterOwnerId)) {
                        responseService.sendError(correlationId, "Permission denied");
                    } else {
                        responseService.sendResponse(petToDto(pet), correlationId);
                    }
                }, () -> responseService.sendError(correlationId, "Pet not found with id: " + id));
    }

    private void updatePet(PetDto dto, String correlationId) {
        repository.findById(dto.getId())
                .ifPresentOrElse(pet -> {
                    if (!pet.getOwnerId().equals(dto.getOwnerId())) {
                        responseService.sendError(correlationId, "Permission denied");
                        return;
                    }
                    pet.setName(dto.getName());
                    pet.setBreed(dto.getBreed());
                    pet.setColor(dto.getColor());
                    pet.setBirthDate(dto.getBirthDate());
                    repository.save(pet);
                    responseService.sendResponse(petToDto(pet), correlationId);
                }, () -> responseService.sendError(correlationId, "Pet not found with id: " + dto.getId()));
    }

    private void deletePet(PetDto dto, String correlationId) {
        repository.findById(dto.getId())
                .ifPresentOrElse(pet -> {
                    if (!pet.getOwnerId().equals(dto.getOwnerId())) {
                        responseService.sendError(correlationId, "Permission denied");
                        return;
                    }
                    repository.delete(pet);
                    responseService.sendResponse(null, correlationId);
                }, () -> responseService.sendError(correlationId, "Pet not found with id: " + dto.getId()));
    }

    private PetDto petToDto(Pet pet) {
        return new PetDto(
                pet.getId(),
                pet.getName(),
                pet.getBreed(),
                pet.getColor(),
                pet.getBirthDate(),
                pet.getOwnerId()
        );
    }
}
