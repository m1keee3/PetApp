package ru.skirda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.skirda.entities.Pet;
import ru.skirda.entities.PetColor;

public interface PetRepository extends JpaRepository<Pet, Long> {
    Page<Pet> findByColor(PetColor color, Pageable pageable);
}
