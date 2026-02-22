package br.com.fiap.file_management.converter;

import br.com.fiap.file_management.domain.User;
import br.com.fiap.file_management.entity.UserEntity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserConverterTest {

    @Test
    void shouldConvertEntityToDomain() {

        UserEntity entity = UserEntity.builder()
                .id(1L)
                .email("user@test.com")
                .password("123")
                .build();

        User domain = UserConverter.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals("user@test.com", domain.getEmail());
    }
}