package com.rhenan.taskflow.infra.persistence.jpa.adapter;

import com.rhenan.taskflow.domain.model.User;
import com.rhenan.taskflow.domain.valueObjects.Email;

import com.rhenan.taskflow.domain.valueObjects.UserId;
import com.rhenan.taskflow.infra.persistence.jpa.entity.UserEntity;
import com.rhenan.taskflow.infra.persistence.jpa.mapper.UserMapper;
import com.rhenan.taskflow.infra.persistence.jpa.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository userJpaRepository;
    
    @Mock
    private UserMapper userMapper;
    
    @InjectMocks
    private UserRepositoryAdapter userRepositoryAdapter;
    
    private User user;
    private UserEntity userEntity;
    private UserId userId;
    private Email email;

    @BeforeEach
    void setUp() {
        userId = new UserId(UUID.randomUUID());
        email = Email.de("teste@email.com");
        user = User.newUser("João Silva", email);
        
        userEntity = new UserEntity();
        userEntity.setId(userId.value());
        userEntity.setName("João Silva");
        userEntity.setEmail("teste@email.com");
    }

    @Test
    void deveSalvarUsuario() {
        when(userMapper.toEntity(user)).thenReturn(userEntity);
        when(userJpaRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        User resultado = userRepositoryAdapter.save(user);

        assertNotNull(resultado);
        assertEquals(user, resultado);
        verify(userMapper).toEntity(user);
        verify(userJpaRepository).save(userEntity);
        verify(userMapper).toDomain(userEntity);
    }

    @Test
    void deveBuscarUsuarioPorId() {
        when(userJpaRepository.findById(userId.value())).thenReturn(Optional.of(userEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        Optional<User> resultado = userRepositoryAdapter.findById(userId);

        assertTrue(resultado.isPresent());
        assertEquals(user, resultado.get());
        verify(userJpaRepository).findById(userId.value());
        verify(userMapper).toDomain(userEntity);
    }

    @Test
    void deveRetornarVazioQuandoUsuarioNaoExistePorId() {
        when(userJpaRepository.findById(userId.value())).thenReturn(Optional.empty());

        Optional<User> resultado = userRepositoryAdapter.findById(userId);

        assertFalse(resultado.isPresent());
        verify(userJpaRepository).findById(userId.value());
        verify(userMapper, never()).toDomain(any());
    }

    @Test
    void deveBuscarUsuarioPorEmail() {
        when(userJpaRepository.findByEmail(email.value())).thenReturn(Optional.of(userEntity));
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        Optional<User> resultado = userRepositoryAdapter.findByEmail(email);

        assertTrue(resultado.isPresent());
        assertEquals(user, resultado.get());
        verify(userJpaRepository).findByEmail(email.value());
        verify(userMapper).toDomain(userEntity);
    }

    @Test
    void deveRetornarVazioQuandoUsuarioNaoExistePorEmail() {
        when(userJpaRepository.findByEmail(email.value())).thenReturn(Optional.empty());

        Optional<User> resultado = userRepositoryAdapter.findByEmail(email);

        assertFalse(resultado.isPresent());
        verify(userJpaRepository).findByEmail(email.value());
        verify(userMapper, never()).toDomain(any());
    }

    @Test
    void deveDeletarUsuarioPorId() {
        userRepositoryAdapter.deleteById(userId);

        verify(userJpaRepository).deleteById(userId.value());
    }

    @Test
    void deveVerificarSeUsuarioExistePorId() {
        when(userJpaRepository.existsById(userId.value())).thenReturn(true);

        boolean resultado = userRepositoryAdapter.existsById(userId);

        assertTrue(resultado);
        verify(userJpaRepository).existsById(userId.value());
    }

    @Test
    void deveRetornarFalsoQuandoUsuarioNaoExistePorId() {
        when(userJpaRepository.existsById(userId.value())).thenReturn(false);

        boolean resultado = userRepositoryAdapter.existsById(userId);

        assertFalse(resultado);
        verify(userJpaRepository).existsById(userId.value());
    }

    @Test
    void deveVerificarSeUsuarioExistePorEmail() {
        when(userJpaRepository.existsByEmail(email.value())).thenReturn(true);

        boolean resultado = userRepositoryAdapter.existsByEmail(email);

        assertTrue(resultado);
        verify(userJpaRepository).existsByEmail(email.value());
    }

    @Test
    void deveRetornarFalsoQuandoUsuarioNaoExistePorEmail() {
        when(userJpaRepository.existsByEmail(email.value())).thenReturn(false);

        boolean resultado = userRepositoryAdapter.existsByEmail(email);

        assertFalse(resultado);
        verify(userJpaRepository).existsByEmail(email.value());
    }
}