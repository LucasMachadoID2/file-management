package br.com.fiap.file_management.converter;

import br.com.fiap.file_management.domain.User;
import br.com.fiap.file_management.entity.UserEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserConverter {

    public static User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .build();
    }
}
