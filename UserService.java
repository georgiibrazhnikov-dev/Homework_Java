package ru.vsu.atm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.atm.domain.model.User;
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

    @Transactional
    public User register(String login, String password) {
        if (userRepository.existsByLogin(login)) {
            throw new UserAlreadyExistsException("User with login '" + login + "' already exists");
        }
        User newUser = new User(login, password);
        return userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public User login(String login, String password) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserLoginNotFoundException("User with login '" + login + "' not found"));
        if (!user.getPassword().equals(password)) {
            throw new InvalidPasswordException("Invalid password");
        }
        return user;
    }
}