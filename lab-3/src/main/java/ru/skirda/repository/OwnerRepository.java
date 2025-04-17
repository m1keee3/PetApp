package ru.skirda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skirda.entities.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
