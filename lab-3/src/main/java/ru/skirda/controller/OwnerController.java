package ru.skirda.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skirda.dto.OwnerDto;
import ru.skirda.entities.Owner;
import ru.skirda.service.OwnerService;

import java.security.Principal;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

    private final OwnerService ownerService;
    private static final Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    public Page<OwnerDto> getAllOwners(Pageable pageable) {
        logger.info("Get all owners");
        return ownerService.getAllOwners(pageable);
    }

    @GetMapping("/{id}")
    public OwnerDto getOwner(@PathVariable("id") Long id) {
        logger.info("Get owner with id: {}", id);
        return ownerService.getOwner(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<OwnerDto> createOwner(@RequestBody OwnerDto ownerDto, Principal principal) {
        logger.info("Create owner request from user '{}'", principal.getName());

        Owner created = ownerService.createOwner(ownerDto, principal.getName());

        logger.info("Owner with id: {} created for user '{}'", created.getId(), principal.getName());
        return new ResponseEntity<>(
                new OwnerDto(created.getId(), created.getName(), created.getBirthDate()),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<OwnerDto> updateOwner(@PathVariable("id") Long id, @RequestBody OwnerDto ownerDto, Principal principal) {
        logger.info("Update owner request for id={} by '{}'", id, principal.getName());

        Owner updated = ownerService.updateOwner(id, ownerDto, principal.getName());

        return ResponseEntity.ok(new OwnerDto(updated.getId(), updated.getName(), updated.getBirthDate()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Void> deleteOwner(@PathVariable("id") Long id, Principal principal) {
        logger.info("Delete owner request for id={} by '{}'", id, principal.getName());

        ownerService.deleteOwner(id, principal.getName());

        logger.info("Owner with id={} deleted by '{}'", id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
