package br.com.fiap.file_management.service;

import br.com.fiap.file_management.entity.UserEntity;

public interface UserService {
    UserEntity register(String email, String rawPassword);

    UserEntity findByEmail(String email);
}
