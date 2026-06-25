package ru.vsu.atm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.atm.dto.AuthRequest;
import ru.vsu.atm.security.JwtService;
import ru.vsu.atm.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        userService.register(request.login(), request.password());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        var user = userService.findByLogin(request.login());
        
        if (!new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
                .matches(request.password(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok("{\"token\":\"" + token + "\"}");
    }
}