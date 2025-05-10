package ru.skirda.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.skirda.entities.User;
import ru.skirda.repository.UserRepository;
import ru.skirda.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setRole(registerRequest.getRole());
        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        Authentication authenticatedUser = authService.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        request.getSession(false);
        SecurityContextHolder.clearContext();
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        return ResponseEntity.ok("Logged out successfully");
    }
}
