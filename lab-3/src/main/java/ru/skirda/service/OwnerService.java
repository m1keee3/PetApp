package ru.skirda.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.skirda.dto.OwnerDto;
import ru.skirda.entities.Owner;
import ru.skirda.entities.Role;
import ru.skirda.entities.User;
import ru.skirda.repository.OwnerRepository;
import ru.skirda.repository.UserRepository;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final UserRepository userRepository;

    @Autowired
    public OwnerService(OwnerRepository ownerRepository, UserRepository userRepository) {
        this.ownerRepository = ownerRepository;
        this.userRepository = userRepository;
    }

    public Page<OwnerDto> getAllOwners(Pageable pageable) {
        return ownerRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    public OwnerDto getOwner(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id " + id));
        return convertToDto(owner);
    }

    public Owner createOwner(OwnerDto ownerDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getOwner() != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Owner already exists for this user");
        }

        Owner owner = new Owner();
        owner.setName(ownerDto.name());
        owner.setBirthDate(ownerDto.birthDate());
        owner.setUser(user);

        Owner saved = ownerRepository.save(owner);
        user.setOwner(saved);
        userRepository.save(user);

        return saved;
    }

    public Owner updateOwner(Long ownerId, OwnerDto ownerDto, String username) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean isAdmin = (user.getRole() == Role.ADMIN);

        boolean isOwnerSelf = owner.getUser() != null &&
                owner.getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwnerSelf) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this owner");
        }

        owner.setName(ownerDto.name());
        owner.setBirthDate(ownerDto.birthDate());

        return ownerRepository.save(owner);
    }


    public void deleteOwner(Long ownerId, String username) {
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean isAdmin = (user.getRole() == Role.ADMIN);

        boolean isOwnerSelf = owner.getUser().getId().equals(user.getId());

        if (!isAdmin && !isOwnerSelf) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this owner");
        }


        owner.getUser().setOwner(null);
        ownerRepository.delete(owner);
    }

    private OwnerDto convertToDto(Owner owner) {
        return new OwnerDto(
                owner.getId(),
                owner.getName(),
                owner.getBirthDate()
        );
    }
}
