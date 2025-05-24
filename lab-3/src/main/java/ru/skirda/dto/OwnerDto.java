package ru.skirda.dto;

import java.time.LocalDate;

public record OwnerDto(
        Long id,
        String name,
        LocalDate birthDate
) {}
