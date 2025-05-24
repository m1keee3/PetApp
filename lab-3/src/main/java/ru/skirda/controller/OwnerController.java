package ru.skirda.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skirda.dto.OwnerDto;
import ru.skirda.entities.Owner;
import ru.skirda.service.OwnerService;

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
    public ResponseEntity<OwnerDto> createOwner(@RequestBody OwnerDto ownerDto) {
        logger.info("Create owner: {}", ownerDto);
        Owner owner = new Owner()
                .setName(ownerDto.name())
                .setBirthDate(ownerDto.birthDate());
        Owner created = ownerService.createOwner(owner);
        logger.info("Owner with id: {} created", created.getId());
        return new ResponseEntity<>(
                new OwnerDto(created.getId(), created.getName(), created.getBirthDate()),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<OwnerDto> updateOwner(@PathVariable("id") Long id, @RequestBody OwnerDto ownerDto) {
        logger.info("Update owner with id: {}, new data: {}", id, ownerDto);
        Owner updated = ownerService.updateOwner(id, ownerDto);
        logger.info("Owner updated with id: {}", updated.getId());
        return ResponseEntity.ok(new OwnerDto(updated.getId(), updated.getName(), updated.getBirthDate()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable("id") Long id) {
        logger.info("Delete owner with id: {}", id);
        ownerService.deleteOwner(id);
        logger.info("Owner with id: {} deleted ", id);
        return ResponseEntity.noContent().build();
    }
}
