package ru.skirda.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skirda.dto.PetDto;
import ru.skirda.entities.Owner;
import ru.skirda.entities.Pet;
import ru.skirda.entities.PetColor;
import ru.skirda.service.PetService;

import java.time.LocalDate;

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
    public ResponseEntity<PetDto> createPet(@RequestBody PetDto petDto) {
        logger.info("Create pet: {}", petDto);
        Pet pet = new Pet().setName(petDto.name())
                .setBirthDate(petDto.birthDate())
                .setBreed(petDto.breed())
                .setColor(PetColor.valueOf(petDto.color().toUpperCase()))
                .setOwner(new Owner(petDto.ownerId(), "", LocalDate.now()));
        Pet created = petService.createPet(pet);
        logger.info("Pet with id: {} created", created.getId());
        return new ResponseEntity<>(new PetDto(created.getId(), created.getName(),
                created.getBreed(), created.getColor().toString(),
                created.getBirthDate(), created.getOwner().getId()),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDto> updatePet(@PathVariable("id") Long id, @RequestBody PetDto petDto) {
        logger.info("Update pet with id: {}, new data: {}", id, petDto);
        Pet updated = petService.updatePet(id, petDto);
        logger.info("pet updated with id: {}", updated.getId());
        return new ResponseEntity<>(new PetDto(updated.getId(), updated.getName(),
                updated.getBreed(), updated.getColor().toString(),
                updated.getBirthDate(), updated.getOwner().getId()),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable("id") Long id) {
        logger.info("Delete pet with id: {}", id);
        petService.deletePet(id);
        logger.info("Pet with id: {} deleted ", id);
        return ResponseEntity.noContent().build();
    }

}
