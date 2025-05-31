package ru.skirda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skirda.model.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {

}