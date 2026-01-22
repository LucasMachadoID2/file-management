package br.com.fiap.file_management.service;

import br.com.fiap.file_management.entity.User;
import br.com.fiap.file_management.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("email já existe");
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .build();

        return userRepository.save(user);
    }
}
