package ru.skirda.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skirda.entities.*;
import ru.skirda.dto.PetDto;
import ru.skirda.repository.PetRepository;
import ru.skirda.repository.UserRepository;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Autowired
    public PetService(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
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

    public Owner getPetOwner(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id " + petId));
        return pet.getOwner();
    }

    public Pet createPet(PetDto petDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Owner owner = user.getOwner();

        if (owner == null) {
            throw new IllegalStateException("User does not have an associated owner profile");
        }

        Pet pet = new Pet()
                .setName(petDto.name())
                .setColor(PetColor.valueOf(petDto.color()))
                .setBreed(petDto.breed())
                .setBirthDate(petDto.birthDate())
                .setOwner(owner);

        return petRepository.save(pet);
    }

    public Pet updatePet(Long petId, PetDto petDto, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean isAdmin = (user.getRole() == Role.ADMIN);

        boolean isOwnerSelf = pet.getOwner() != null &&
                pet.getOwner().getUser() != null &&
                pet.getOwner().getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwnerSelf) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this pet");
        }

        pet.setName(petDto.name())
                .setBreed(petDto.breed())
                .setColor(PetColor.valueOf(petDto.color()))
                .setBirthDate(petDto.birthDate())
                .setBirthDate(petDto.birthDate());

        return petRepository.save(pet);
    }


    public void deletePet(Long petId, String username) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean isAdmin = (user.getRole() == Role.ADMIN);

        Owner petOwner = pet.getOwner();
        boolean isOwnerSelf = petOwner != null
                && petOwner.getUser() != null
                && petOwner.getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwnerSelf) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this pet");
        }

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
