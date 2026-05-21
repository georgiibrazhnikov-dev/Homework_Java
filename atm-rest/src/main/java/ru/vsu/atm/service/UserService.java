package ru.vsu.atm.service;

import org.springframework.stereotype.Service;
import ru.vsu.atm.domain.entity.UserEntity;
import ru.vsu.atm.exception.InvalidPasswordException;
import ru.vsu.atm.exception.UserAlreadyExistsException;
import ru.vsu.atm.exception.UserLoginNotFoundException;
import ru.vsu.atm.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity register(String login, String password) {
        if (userRepository.existsByLogin(login)) {
            throw new UserAlreadyExistsException("User with login '" + login + "' already exists");
        }
        UserEntity user = new UserEntity(null, login, password);
        return userRepository.save(user);
    }

    public UserEntity login(String login, String password) {
        UserEntity user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserLoginNotFoundException("User not found"));
        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException("Invalid password");
        }
        return user;
    }

    public UserEntity findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserLoginNotFoundException("User not found"));
    }
}