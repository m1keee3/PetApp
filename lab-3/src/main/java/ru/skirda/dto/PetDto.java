package ru.skirda.dto;

import java.time.LocalDate;

public record PetDto(
        Long id,
        String name,
        String breed,
        String color,
        LocalDate birthDate,
        Long ownerId
) {}
