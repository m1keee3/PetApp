package ru.skirda.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "owners")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate birthDate;
    private Long userId;

    public Owner(String name, LocalDate birthDate, Long userId) {
        this.name = name;
        this.birthDate = birthDate;
        this.userId = userId;
    }
}