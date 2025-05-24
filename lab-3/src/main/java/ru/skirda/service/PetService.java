package ru.skirda.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skirda.entities.Pet;
import ru.skirda.entities.PetColor;
import ru.skirda.dto.PetDto;
import ru.skirda.repository.PetRepository;

@Service
public class PetService {

    private final PetRepository petRepository;

    @Autowired
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Page<PetDto> getPets(String color, Pageable pageable) {
        Page<Pet> petsPage;
        if (color != null) {
            petsPage = petRepository.findByColor(PetColor.valueOf(color.toUpperCase()), pageable);
        } else {
            petsPage = petRepository.findAll(pageable);
        }
        return petsPage.map(this::convertToDto);
    }

    public PetDto getPet(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id " + id));
        return convertToDto(pet);
    }

    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    public Pet updatePet(Long id, PetDto petDto) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id " + id));
        pet.setName(petDto.name())
                .setBreed(petDto.breed())
                .setColor(PetColor.valueOf(petDto.color().toUpperCase()))
                .setBirthDate(petDto.birthDate());
        return petRepository.save(pet);
    }

    public void deletePet(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id " + id));
        petRepository.delete(pet);
    }


    private PetDto convertToDto(Pet pet) {
        return new PetDto(
                pet.getId(),
                pet.getName(),
                pet.getBreed(),
                pet.getColor().toString(),
                pet.getBirthDate(),
                pet.getOwner().getId()
        );
    }
}
