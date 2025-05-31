package ru.skirda.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OwnerDto {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private Long userId;

    private Integer page;
    private Integer size;

    public OwnerDto(Long id, String name, LocalDate birthDate, Long userId) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.userId = userId;
    }
}