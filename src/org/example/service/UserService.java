package org.example.service;

import org.example.domain.model.User;
import org.example.exception.InvalidPasswordException;
import org.example.exception.UserAlreadyExistsException;
import org.example.exception.UserLoginNotFoundException;
import org.example.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Регистрация нового пользователя
    public User register(String login, String password) {
        if (userRepository.existsByLogin(login)) {
            throw new UserAlreadyExistsException("User with login '" + login + "' already exists");
        }
        User newUser = new User(login, password);
        userRepository.save(newUser);
        return newUser;
    }

    // Авторизация
    public User login(String login, String password) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserLoginNotFoundException("User with login '" + login + "' not found"));
        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException("Invalid password");
        }
        return user;
    }
}