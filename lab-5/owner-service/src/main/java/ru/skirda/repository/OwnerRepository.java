package ru.skirda.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skirda.model.Owner;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByUserId(Long userId);
}