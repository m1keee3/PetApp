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
import ru.skirda.dto.PetDto;
import ru.skirda.entities.Pet;
import ru.skirda.service.PetService;

import java.security.Principal;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;
    private static final Logger logger = LoggerFactory.getLogger(OwnerController.class);

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public Page<PetDto> getPets(
            @RequestParam(required = false) String color,
            Pageable pageable) {
        return petService.getPets(color, pageable);
    }

    @GetMapping("/{id}")
    public PetDto getPet(@PathVariable("id") Long id) {
        logger.info("Get pet with id: {}", id);
        return petService.getPet(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PetDto> createPet(@RequestBody PetDto petDto, Principal principal) {
        logger.info("Create pet: {}", petDto);

        Pet createdPet = petService.createPet(petDto, principal.getName());

        logger.info("Pet with id: {} created", createdPet.getId());
        return new ResponseEntity<>(
                new PetDto(createdPet.getId(), createdPet.getName(), createdPet.getBreed(), createdPet.getColor().toString(), createdPet.getBirthDate(), createdPet.getOwner().getId()),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<PetDto> updatePet(@PathVariable("id") Long id, @RequestBody PetDto petDto, Principal principal) {
        logger.info("Update pet request for id={} by '{}'", id, principal.getName());

        Pet updated = petService.updatePet(id, petDto, principal.getName());

        return ResponseEntity.ok(new PetDto(updated.getId(), updated.getName(), updated.getBreed(), updated.getColor().toString(), updated.getBirthDate(), updated.getOwner().getId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<Void> deletePet(@PathVariable("id") Long id, Principal principal) {
        logger.info("Delete pet request for id={} by '{}'", id, principal.getName());

        petService.deletePet(id, principal.getName());

        logger.info("Pet with id={} deleted by '{}'", id, principal.getName());
        return ResponseEntity.noContent().build();
    }

}
