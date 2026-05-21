package ru.vsu.atm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.atm.domain.entity.UserEntity;
import ru.vsu.atm.dto.request.LoginRequest;
import ru.vsu.atm.dto.response.UserResponse;
import ru.vsu.atm.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        UserEntity user = userService.login(request.getLogin(), request.getPassword());
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getLogin()));
    }
}