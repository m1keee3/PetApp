package ru.skirda.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.skirda.dto.PetDto;
import ru.skirda.gateway.model.CustomUserDetails;
import ru.skirda.gateway.service.KafkaService;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final KafkaService kafkaService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public CompletableFuture<PetDto> getPet(@PathVariable("id") Long id,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        PetDto request = new PetDto();
        request.setId(id);
        request.setOwnerId(userDetails.getId());
        return kafkaService.sendRequest("pet-requests", "getPetById", request, PetDto.class);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<PetDto>> createPet(
            @RequestBody PetDto petDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        petDto.setOwnerId(userDetails.getId());
        return kafkaService.sendRequest("pet-requests", "createPet", petDto, PetDto.class)
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<PetDto>> updatePet(
            @PathVariable("id") Long id,
            @RequestBody PetDto petDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        petDto.setId(id);
        petDto.setOwnerId(userDetails.getId());
        return kafkaService.sendRequest("pet-requests", "updatePet", petDto, PetDto.class)
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Void>> deletePet(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        PetDto request = new PetDto();
        request.setId(id);
        request.setOwnerId(userDetails.getId());

        return kafkaService.sendRequest("pet-requests", "deletePet", request, Void.class)
                .thenApply(response -> ResponseEntity.noContent().build());
    }
}
