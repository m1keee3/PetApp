package ru.skirda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {
    private Long id;
    private String name;
    private String breed;
    private String color;
    private LocalDate birthDate;
    private Long ownerId;

    private Integer page;
    private Integer size;

    public PetDto(Long id, String name, String breed, String color, LocalDate birthDate, Long ownerId) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.color = color;
        this.birthDate = birthDate;
        this.ownerId = ownerId;
    }
}

