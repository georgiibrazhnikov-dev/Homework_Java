package ru.vsu.atm.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vsu.atm.domain.entity.UserEntity;
import ru.vsu.atm.dto.request.RegisterRequest;
import ru.vsu.atm.dto.response.UserResponse;
import ru.vsu.atm.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        UserEntity user = userService.register(request.getLogin(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(user.getId(), user.getLogin()));
    }
}