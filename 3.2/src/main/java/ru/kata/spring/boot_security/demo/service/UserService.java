package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    User getUser(long id);

    void saveUser(User user);

    void removeUserById(long id);

    void editUser(Long id, User user);

    Optional<User> findByUsername(String username);
}
