package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // получить список пользователей из БД
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //показать пользователя по ID

    @Override
    @Transactional
    public User getUser(long id) {
        return userRepository.findById(id).get();
    }

    //сохранить пользователя
    @Transactional
    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    //удалить пользователя по ID
    @Transactional
    @Override
    public void removeUserById(long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }

    // Редактирование пользователя
    @Transactional
    @Override
    public void editUser(Long id, User user) {
        User existingUser = userRepository.findById(id).get(); // получаем существующего пользователя из репозитория
        if (user.getPassword().isEmpty()) { // если пароль пустой
            user.setPassword(existingUser.getPassword());// то пароль будет равным паролю существующего пользователя.
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // иначе закодировать пароль нового пользователя с использованием passwordEncoder
        }
        userRepository.save(user); // сохраняем нового пользователя в репозиторий
    }

    //получение пользователя по имени
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = findByUsername(username);
        if (user.isEmpty()) { // если пользователя нет, то выбрасывается исколючение
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return user.get();
    }
}
