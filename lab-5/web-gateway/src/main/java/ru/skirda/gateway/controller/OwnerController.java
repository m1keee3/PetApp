package ru.skirda.gateway.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.skirda.dto.OwnerDto;
import ru.skirda.gateway.model.CustomUserDetails;
import ru.skirda.gateway.service.KafkaService;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final KafkaService kafkaService;

    @GetMapping("/{id}")
    public CompletableFuture<OwnerDto> getOwner(@PathVariable("id") Long id) {
        OwnerDto request = new OwnerDto();
        request.setId(id);
        return kafkaService.sendRequest("owner-requests", "getOwnerById", request, OwnerDto.class);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<OwnerDto>> createOwner(
            @RequestBody OwnerDto ownerDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ownerDto.setUserId(userDetails.getId());
        return kafkaService.sendRequest("owner-requests", "createOwner", ownerDto, OwnerDto.class)
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<OwnerDto>> updateOwner(
            @PathVariable("id") Long id,
            @RequestBody OwnerDto ownerDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ownerDto.setId(id);
        ownerDto.setUserId(userDetails.getId());
        return kafkaService.sendRequest("owner-requests", "updateOwner", ownerDto, OwnerDto.class)
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<Void>> deleteOwner(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        OwnerDto request = new OwnerDto();
        request.setId(id);
        request.setUserId(userDetails.getId());

        return kafkaService.sendRequest("owner-requests", "deleteOwner", request, Void.class)
                .thenApply(response -> ResponseEntity.noContent().build());
    }
}
