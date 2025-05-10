package ru.skirda.controller;

import lombok.Data;
import ru.skirda.entities.Role;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}
