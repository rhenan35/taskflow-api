package com.rhenan.taskflow.infra.persistence.jpa.mapper;

import com.rhenan.taskflow.domain.model.User;
import com.rhenan.taskflow.domain.valueObjects.Email;
import com.rhenan.taskflow.domain.valueObjects.Name;
import com.rhenan.taskflow.infra.persistence.jpa.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void deveConverterUserParaEntity() {
        Name name = new Name("João Silva");
        Email email = Email.de("joao@email.com");
        User user = User.newUser(name, email);

        UserEntity entity = userMapper.toEntity(user);

        assertNotNull(entity);
        assertEquals(user.getId().value(), entity.getId());
        assertEquals("João Silva", entity.getName());
        assertEquals("joao@email.com", entity.getEmail());
    }

    @Test
    void deveRetornarNullQuandoUserForNull() {
        UserEntity entity = userMapper.toEntity(null);
        
        assertNull(entity);
    }

    @Test
    void deveConverterEntityParaUser() {
        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setName("Maria Santos");
        entity.setEmail("maria@email.com");

        User user = userMapper.toDomain(entity);

        assertNotNull(user);
        assertEquals("Maria Santos", user.getName().value());
        assertEquals("maria@email.com", user.getEmail().value());
    }

    @Test
    void deveRetornarNullQuandoEntityForNull() {
        User user = userMapper.toDomain(null);
        
        assertNull(user);
    }

    @Test
    void deveManterConsistenciaEntreConversoes() {
        Name name = new Name("Pedro Costa");
        Email email = Email.de("pedro@email.com");
        User originalUser = User.newUser(name, email);

        UserEntity entity = userMapper.toEntity(originalUser);
        User convertedUser = userMapper.toDomain(entity);

        assertEquals(originalUser.getName().value(), convertedUser.getName().value());
        assertEquals(originalUser.getEmail().value(), convertedUser.getEmail().value());
    }
}